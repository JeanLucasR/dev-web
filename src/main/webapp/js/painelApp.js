const { createApp } = Vue;

createApp({
    data(){
        return {
            atual: null,
            ultimas: [],
            versao: null,
            destacar: false,
            semConexao: false,
            temporizadorDestaque: null
        };
    },

    mounted(){
        this.carregarPainel();
        setInterval(this.carregarPainel, 2000);
    },

    methods: {

        async carregarPainel(){
            try {
                const resposta = await fetch('painel');
                const dados = await resposta.json();

                if(!resposta.ok)
                    throw new Error("erro ao carregar o painel.");

                if(this.versao !== null && dados.versao !== this.versao)
                    this.destacarChamada();

                this.versao = dados.versao;
                this.atual = dados.atual;
                this.ultimas = dados.ultimas || [];
                this.semConexao = false;
            } catch(erro){
                console.log("Falha ao atualizar o painel:", erro);
                this.semConexao = true;
            }
        },

        destacarChamada(){
            this.destacar = true;
            clearTimeout(this.temporizadorDestaque);
            this.temporizadorDestaque = setTimeout(() => {
                this.destacar = false;
            }, 5000);
        }
    }
}).mount('#app')