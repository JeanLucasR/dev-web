const { createApp } = Vue;

createApp({
    data(){
        return {
            etapa: 'tipo',

            novo: {tipo: '', prioridade: false},
            senha: {codigo: '', fila: 0, tipo: '', prioridade: false},
            mensagem: '',
            houveErro: false,
            salvando: false
        };
    },

    methods: {

        descricaoTipo(tipo){
            let descricao = '';

            if (tipo === 'C')
                descricao = 'Renovação CNH';
            else if (tipo === 'E')
                descricao = 'Emissão documento';
            else if (tipo === 'LIC')
                descricao = 'Licenciamento';

            return descricao;
        },

        selecionarTipo(tipo) {
            this.novo.tipo = tipo;
            this.etapa = 'prioridade';
        },

        selecionarPrioridade(prioridade) {
            this.novo.prioridade = prioridade;
            this.etapa = 'confirmacao';
        },

        voltarParaTipo() {
            this.novo.tipo = '';
            this.novo.prioridade = false;
            this.etapa = 'tipo';
        },

        async novaSenha(){
            if (this.salvando) return;

            this.salvando = true;
            this.etapa = 'carregando';

            const solicitacao = { ...this.novo };

            this.mensagem = '';
            this.houveErro = false;

            try {
                const resposta = await fetch('usuarioSenha', {
                    method: 'POST',
                    headers: { 'Content-Type': 'application/json' },
                    body: JSON.stringify(solicitacao)
                });

                const dados = await resposta.json();

                console.log("Status HTTP:", resposta.status);
                console.log("Resposta do servidor:", dados);

                if (!resposta.ok)
                    throw new Error(dados.erro || "Erro ao requisitar senha.");

                this.senha.codigo = dados.codigo;
                this.senha.fila = dados.pessoasNaFrente;
                this.senha.tipo = solicitacao.tipo;
                this.senha.prioridade = solicitacao.prioridade;
                this.mensagem = dados.mensagem;

                this.novo = {tipo: '', prioridade: false};

            } catch(erro){
                this.houveErro = true;
                this.mensagem = erro.message;

            } finally {
                this.salvando = false;
                this.etapa = 'resultado';
            }
        },

        reiniciar() {
            this.novo = {tipo: '', prioridade: false};
            this.senha = {codigo: '', fila: 0, tipo: '', prioridade: false};
            this.mensagem = '';
            this.houveErro = false;
            this.etapa = 'tipo';
            this.salvando = false;
        }
    }
}).mount('#app')