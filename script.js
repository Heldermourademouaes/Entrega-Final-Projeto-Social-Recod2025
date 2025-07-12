
// Função para carregar conteúdo via fetch
async function loadContent() {
  try {
    // Carregar menu
    const menuResponse = await fetch("menu.html");
    const menuData = await menuResponse.text();
    const header = document.querySelector("header");
    header.innerHTML = menuData;

    // Carregar footer
    const footerResponse = await fetch("footer.html");
    const footerData = await footerResponse.text();
    document.querySelector("footer").innerHTML = footerData;

    // Aguardar um pouco para garantir que o DOM seja atualizado
    await new Promise(resolve => setTimeout(resolve, 100));

    // Disparar evento personalizado
    document.dispatchEvent(new Event("menuCarregado"));

    // Inicializar ou atualizar o TextReader após carregar tudo
    if (textReader) {
      textReader.refreshContent();
    } else {
      textReader = new TextReader();
    }

    document.getElementById("leitor-voz").addEventListener("click", function () {
      const box = document.querySelector(".reader-controls");
      if (box) {
        box.classList.add("show");
        this.classList.add("hide");
      }
    });
    document.querySelector(".fechar-reader-controls").addEventListener("click", function () {
      const box = document.querySelector(".reader-controls");
      const botao = document.querySelector("#leitor-voz");
      if (box) {
        box.classList.remove("show");
        botao.classList.remove("hide");
      }
    });
  } catch (error) {
    console.error("Erro ao carregar conteúdo:", error);

    // Inicializar TextReader mesmo se o fetch falhar
    if (!textReader) {
      textReader = new TextReader();
    }
  }
}

// Inicializar quando a página carregar
document.addEventListener('DOMContentLoaded', loadContent);
