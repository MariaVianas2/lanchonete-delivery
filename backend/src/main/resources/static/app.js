const API = "http://localhost:8080";

// --- helpers ---
function setMsg(id, text) {
  const el = document.getElementById(id);
  if (el) el.innerText = text;
}

function salvarUsuario(nome, email) {
  localStorage.setItem("user_nome", nome || "");
  localStorage.setItem("user_email", email || "");
}

function mostrarUsuario() {
  const nome = localStorage.getItem("user_nome") || "";
  const email = localStorage.getItem("user_email") || "";
  const el = document.getElementById("userWelcome");
  if (el) el.innerText = nome ? `Logado como: ${nome} (${email})` : `Logado como: ${email}`;
}

function protegerPagina() {
  const email = localStorage.getItem("user_email");
  if (!email) window.location.href = "index.html";
}

function logout() {
  localStorage.removeItem("user_nome");
  localStorage.removeItem("user_email");
  window.location.href = "index.html";
}

// --- AUTH ---
async function cadastrar() {
  const nome = document.getElementById("cadNome").value.trim();
  const email = document.getElementById("cadEmail").value.trim();
  const senha = document.getElementById("cadSenha").value.trim();

  const resp = await fetch(`${API}/auth/cadastro`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nome, email, senha })
  });

  const txt = await resp.text();
  setMsg("cadMsg", txt);
}

async function login() {
  const email = document.getElementById("loginEmail").value.trim();
  const senha = document.getElementById("loginSenha").value.trim();

  const resp = await fetch(`${API}/auth/login`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ email, senha })
  });

  const txt = await resp.text();

  if (resp.ok) {
    let nome = "";
    if (txt.includes(",")) nome = txt.split(",")[1].trim();
    salvarUsuario(nome, email);
    window.location.href = "produtos.html";
  } else {
    setMsg("loginMsg", txt);
  }
}

// --- PRODUTOS ---
async function carregarProdutos() {
  const ul = document.getElementById("listaProdutos");
  if (!ul) return;

  ul.innerHTML = "<li>Carregando...</li>";

  const resp = await fetch(`${API}/produtos`);
  const lista = await resp.json();

  ul.innerHTML = "";
  lista.forEach(p => {
    const li = document.createElement("li");
    li.style.display = "flex";
    li.style.gap = "12px";
    li.style.alignItems = "center";
    li.style.padding = "10px";
    li.style.border = "1px solid #ddd";
    li.style.borderRadius = "10px";
    li.style.marginBottom = "10px";

    const img = document.createElement("img");
    img.src = p.imagemUrl || "https://via.placeholder.com/70";
    img.alt = p.nome;
    img.style.width = "70px";
    img.style.height = "70px";
    img.style.objectFit = "cover";
    img.style.borderRadius = "10px";
    img.onerror = () => img.src = "https://via.placeholder.com/70";

    const info = document.createElement("div");
    info.innerHTML = `
      <b>#${p.id} - ${p.nome}</b><br/>
      <span>R$ ${Number(p.preco).toFixed(2)}</span><br/>
      <small>${p.descricao || ""}</small>
    `;

    li.appendChild(img);
    li.appendChild(info);
    ul.appendChild(li);
  });
}

async function criarProduto() {
  const nome = document.getElementById("pNome").value.trim();
  const descricao = document.getElementById("pDesc").value.trim();
  const preco = Number(document.getElementById("pPreco").value);
  const imagemUrl = (document.getElementById("pImg")?.value || "").trim();

  if (!nome) return setMsg("produtoMsg", "Nome é obrigatório.");
  if (!preco || preco <= 0) return setMsg("produtoMsg", "Preço inválido.");

  const resp = await fetch(`${API}/produtos`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nome, descricao, preco, imagemUrl })
  });

  if (resp.ok) {
    setMsg("produtoMsg", "Produto criado ✅");
    document.getElementById("pNome").value = "";
    document.getElementById("pDesc").value = "";
    document.getElementById("pPreco").value = "";
    if (document.getElementById("pImg")) document.getElementById("pImg").value = "";
    carregarProdutos();
  } else {
    const txt = await resp.text();
    setMsg("produtoMsg", "Erro: " + txt);
  }
}

// --- CLIENTES ---
async function carregarClientes() {
  const ul = document.getElementById("listaClientes");
  if (!ul) return;

  ul.innerHTML = "<li>Carregando...</li>";
  const resp = await fetch(`${API}/clientes`);
  const lista = await resp.json();

  ul.innerHTML = "";
  lista.forEach(c => {
    const li = document.createElement("li");
    li.innerText = `#${c.id} - ${c.nome} (${c.telefone})`;
    ul.appendChild(li);
  });
}

async function criarCliente() {
  const nome = document.getElementById("cNome").value.trim();
  const telefone = document.getElementById("cTel").value.trim();
  const endereco = document.getElementById("cEnd").value.trim();

  if (!nome) return setMsg("clienteMsg", "Nome é obrigatório.");

  const resp = await fetch(`${API}/clientes`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify({ nome, telefone, endereco })
  });

  if (resp.ok) {
    setMsg("clienteMsg", "Cliente criado ✅");
    document.getElementById("cNome").value = "";
    document.getElementById("cTel").value = "";
    document.getElementById("cEnd").value = "";
    carregarClientes();
  } else {
    const txt = await resp.text();
    setMsg("clienteMsg", "Erro: " + txt);
  }
}

// --- PEDIDOS ---
let itensPedido = []; // { produtoId, quantidade }

async function iniciarTelaPedidos() {
  await carregarSelectClientes();
  await carregarSelectProdutos();
  await carregarPedidos();
}

async function carregarSelectClientes() {
  const sel = document.getElementById("clienteSelect");
  if (!sel) return;

  const resp = await fetch(`${API}/clientes`);
  const lista = await resp.json();

  sel.innerHTML = "";
  lista.forEach(c => {
    const opt = document.createElement("option");
    opt.value = c.id;
    opt.textContent = `#${c.id} - ${c.nome}`;
    sel.appendChild(opt);
  });
}

async function carregarSelectProdutos() {
  const sel = document.getElementById("produtoSelect");
  if (!sel) return;

  const resp = await fetch(`${API}/produtos`);
  const lista = await resp.json();

  sel.innerHTML = "";
  lista.forEach(p => {
    const opt = document.createElement("option");
    opt.value = p.id;
    opt.textContent = `#${p.id} - ${p.nome} (R$ ${Number(p.preco).toFixed(2)})`;
    sel.appendChild(opt);
  });
}

function adicionarItem() {
  const produtoId = Number(document.getElementById("produtoSelect").value);
  const quantidade = Number(document.getElementById("qtdInput").value);

  if (!produtoId || quantidade <= 0) {
    setMsg("pedidoMsg", "Selecione um produto e uma quantidade válida.");
    return;
  }

  itensPedido.push({ produtoId, quantidade });
  renderItens();
  setMsg("pedidoMsg", "");
}

function renderItens() {
  const ul = document.getElementById("itensLista");
  if (!ul) return;

  ul.innerHTML = "";
  itensPedido.forEach((it, idx) => {
    const li = document.createElement("li");
    li.innerHTML = `Produto #${it.produtoId} — Qtd: ${it.quantidade}
      <button class="btn-secondary" style="width:auto; margin-left:10px;" onclick="removerItem(${idx})">Remover</button>`;
    ul.appendChild(li);
  });
}

function removerItem(index) {
  itensPedido.splice(index, 1);
  renderItens();
}

async function criarPedido() {
  const clienteId = Number(document.getElementById("clienteSelect").value);

  if (!clienteId) return setMsg("pedidoMsg", "Selecione um cliente.");
  if (itensPedido.length === 0) return setMsg("pedidoMsg", "Adicione pelo menos 1 item.");

  const body = {
    clienteId,
    itens: itensPedido
  };

  const resp = await fetch(`${API}/pedidos`, {
    method: "POST",
    headers: { "Content-Type": "application/json" },
    body: JSON.stringify(body)
  });

  if (resp.ok) {
    setMsg("pedidoMsg", "Pedido criado ✅");
    itensPedido = [];
    renderItens();
    carregarPedidos();
  } else {
    const txt = await resp.text();
    setMsg("pedidoMsg", "Erro: " + txt);
  }
}

async function carregarPedidos() {
  const ul = document.getElementById("pedidosLista");
  if (!ul) return;

  ul.innerHTML = "<li>Carregando...</li>";

  const resp = await fetch(`${API}/pedidos`);
  const lista = await resp.json();

  ul.innerHTML = "";
  lista.forEach(p => {
    const li = document.createElement("li");
    li.innerText = `Pedido #${p.id} — Status: ${p.status}`;
    ul.appendChild(li);
  });
}

// --- inicialização por página ---
document.addEventListener("DOMContentLoaded", () => {
  const path = window.location.pathname;

  // protege todas as páginas exceto index.html e a raiz
  if (!path.endsWith("index.html") && !path.endsWith("/")) {
    protegerPagina();
    mostrarUsuario();
  }

  if (path.endsWith("produtos.html")) carregarProdutos();
  if (path.endsWith("clientes.html")) carregarClientes();
  if (path.endsWith("pedidos.html")) iniciarTelaPedidos();
});
