--
-- Banco de dados: `web_mp2`
--

-- --------------------------------------------------------

--
-- Estrutura para tabela `controle_fila`
--

CREATE TABLE `controle_fila` (
  `tipo` varchar(20) NOT NULL,
  `prioritarias_consecutivas` int(11) NOT NULL DEFAULT 0
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Despejando dados para a tabela `controle_fila`
--

INSERT INTO `controle_fila` (`tipo`, `prioritarias_consecutivas`) VALUES
('C', 0),
('E', 0),
('LIC', 0);

-- --------------------------------------------------------

--
-- Estrutura para tabela `guiches`
--

CREATE TABLE `guiches` (
  `ID` int(11) NOT NULL,
  `atende_cnh` tinyint(1) DEFAULT NULL,
  `atende_emissao` tinyint(1) DEFAULT NULL,
  `atende_processo` tinyint(1) DEFAULT NULL,
  `abertura` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

-- --------------------------------------------------------

--
-- Estrutura para tabela `senhas`
--

CREATE TABLE `senhas` (
  `ID` int(11) NOT NULL,
  `codigo` varchar(20) DEFAULT NULL,
  `prioridade` tinyint(1) DEFAULT NULL,
  `tipo` varchar(20) DEFAULT NULL,
  `stat` varchar(50) DEFAULT 'AGUARDANDO',
  `emissao` datetime DEFAULT NULL
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_general_ci;

--
-- Índices para tabelas despejadas
--

--
-- Índices de tabela `controle_fila`
--
ALTER TABLE `controle_fila`
  ADD PRIMARY KEY (`tipo`);

--
-- Índices de tabela `guiches`
--
ALTER TABLE `guiches`
  ADD PRIMARY KEY (`ID`);

--
-- Índices de tabela `senhas`
--
ALTER TABLE `senhas`
  ADD PRIMARY KEY (`ID`);

--
-- AUTO_INCREMENT para tabelas despejadas
--

--
-- AUTO_INCREMENT de tabela `guiches`
--
ALTER TABLE `guiches`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT;

--
-- AUTO_INCREMENT de tabela `senhas`
--
ALTER TABLE `senhas`
  MODIFY `ID` int(11) NOT NULL AUTO_INCREMENT, AUTO_INCREMENT=17;
COMMIT;