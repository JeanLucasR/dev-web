const { createApp } = Vue;

createApp({
    data() {
        return {
            id: "",
            CNH: false,
            emissao: false,
            LIC: false,

            carregando: false,
            mensagem: "",
        };
    },

    methods: {
        async iniciarGuiche() {

            this.mensagem = "";

            
            if (this.id !== "1" && this.id !== "2") {
                this.mensagem = "Escolha um guichê valido (1 ou 2).";
                return;
            }

           
            if (!this.CNH && !this.emissao && !this.LIC) {
                this.mensagem = "Selecione pelo menos um tipo de atendimento.";
                return;
            }

            this.carregando = true;

            const parametros = new URLSearchParams();

            parametros.append("id", this.id);
            parametros.append("CNH", this.CNH);
            parametros.append("emissao", this.emissao);
            parametros.append("LIC", this.LIC);

            try {
                const resposta = await fetch("iniciarGuiche", {
                    method: "POST",
                    headers: {"Content-Type": "application/x-www-form-urlencoded"},
                    body: parametros.toString()
                });

                if (resposta.redirected) {
                    window.location.href = resposta.url;
                    return;
                }

                const dados = await resposta.json();

                if (!resposta.ok) {
                    throw new Error(dados.erro || "Não foi possível iniciar o guichê.");
                }

                this.mensagem =  "Guichê iniciado com sucesso!";

            } catch (erro) {
                this.mensagem = erro.message;
            } finally {
                this.carregando = false;
            }
        },
    }
}).mount("#app");