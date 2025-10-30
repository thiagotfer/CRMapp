package com.crmapp.data

import com.crmapp.data.models.Client
import com.crmapp.data.models.Coupon
import com.crmapp.data.models.Interaction
import com.crmapp.data.models.User

val mockOperator = User(id = "op_01", name = "João Operador", isOperator = true)

val mockClientUser = User(
    id = "cli_01",
    name = "Ana Maria Silva",
    isOperator = false,
    coupons = listOf(
        Coupon(code = "PROMO20", description = "20% de desconto em todo o site", expiryDate = "31/12/2025"),
        Coupon(code = "FRETEGRATIS", description = "Frete grátis para compras acima de R$150", expiryDate = "30/11/2025"),
        Coupon(code = "PRIMEIRACOMPRA", description = "10% de desconto na sua primeira compra", expiryDate = "Válido por 7 dias")
    )
)

val mockClients = listOf(
    Client("cli_01", "Ana Maria Silva", "Cliente em Risco", "Alto Risco", "Há 4h", 2, listOf("Risco", "Suporte"), interactions = listOf(Interaction(type = "Anotação", content = "Reclamou da velocidade."))),
    Client("cli_02", "Joaquim Souza", "Novo", "Novos Clientes", "Há 7min", 4, listOf("Interessado"), interactions = listOf(Interaction(type = "Anotação", content = "Pediu demonstração."))),
    Client("cli_03", "Tadeu Oliveira", "Inativo", "Ex-Clientes", "Há 3d", 1, listOf("Ex-cliente"), interactions = listOf(Interaction(type = "Anotação", content = "Cancelou por preço."))),
    Client("cli_04", "Jorge Costa", "Ativo", "Clientes VIP", "Há 30min", 5, listOf("VIP", "Fidelizado"), interactions = listOf(Interaction(type = "Anotação", content = "Comprou novo pacote."))),
    Client("cli_05", "Fabio Andrade", "Ativo", "Em Negociação", "Há 1h", 4, listOf("Contrato_Pendente"), interactions = emptyList()),
    Client("cli_06", "Beatriz Lima", "Ativo", "Clientes VIP", "Ontem", 5, listOf("Fidelizado", "Upgrade"), interactions = listOf(Interaction(type = "Anotação", content = "Sempre elogia o atendimento."))),
    Client("cli_07", "Ricardo Alves", "Novo", "Novos Clientes", "Há 2h", 3, listOf("Onboarding"), interactions = listOf(Interaction(type = "Anotação", content = "Finalizando cadastro."))),
    Client("cli_08", "Fernanda Martins", "Cliente em Risco", "Alto Risco", "Há 2d", 2, listOf("Inadimplente"), interactions = listOf(Interaction(type = "Anotação", content = "Boleto em atraso."))),
    Client("cli_09", "Lucas Gonçalves", "Ativo", "Clientes Padrão", "Hoje", 4, listOf("Engajado"), interactions = listOf(Interaction(type = "Anotação", content = "Abriu o email da campanha."))),
    Client("cli_10", "Mariana Ferreira", "Inativo", "Ex-Clientes", "Há 1 mês", 1, listOf(), interactions = listOf(Interaction(type = "Anotação", content = "Mudou de cidade."))),
    Client("cli_11", "Gustavo Pereira", "Ativo", "Clientes VIP", "Há 5h", 5, listOf("VIP"), interactions = listOf(Interaction(type = "Anotação", content = "Renovou o plano anual."))),
    Client("cli_12", "Camila Ribeiro", "Ativo", "Clientes Padrão", "Há 8h", 3, listOf(), interactions = listOf(Interaction(type = "Anotação", content = "Contato esporádico."))),
    Client("cli_13", "Vinicius Barros", "Cliente em Risco", "Alto Risco", "Há 5d", 2, listOf("Suporte", "Reclamacao"), interactions = listOf(Interaction(type = "Anotação", content = "Problema técnico não resolvido."))),
    Client("cli_14", "Larissa Santos", "Novo", "Novos Clientes", "Há 1d", 4, listOf("Interessado"), interactions = emptyList()),
    Client("cli_15", "Eduardo Castro", "Ativo", "Em Negociação", "Hoje", 3, listOf("Proposta Enviada"), interactions = listOf(Interaction(type = "Anotação", content = "Analisando proposta."))),
    Client("cli_16", "Juliana Azevedo", "Ativo", "Clientes Padrão", "Há 6h", 4, listOf(), interactions = listOf(Interaction(type = "Anotação", content = "Satisfeita."))),
    Client("cli_17", "Rafael Moreira", "Ativo", "Clientes VIP", "Há 15min", 5, listOf("VIP", "Promotor"), interactions = listOf(Interaction(type = "Anotação", content = "Indicou 2 novos clientes."))),
    Client("cli_18", "Patrícia Nunes", "Cliente em Risco", "Alto Risco", "Há 1 sem", 1, listOf("Inadimplente"), interactions = listOf(Interaction(type = "Anotação", content = "Não responde contatos."))),
    Client("cli_19", "Thiago Mendes", "Novo", "Novos Clientes", "Ontem", 3, listOf("Onboarding"), interactions = emptyList()),
    Client("cli_20", "Amanda Correia", "Ativo", "Clientes Padrão", "Há 2d", 3, listOf(), interactions = emptyList()),
    Client("cli_21", "Felipe Rocha", "Ativo", "Em Negociação", "Há 3h", 4, listOf("Contrato_Pendente"), interactions = listOf(Interaction(type = "Anotação", content = "Pediu ajuste no contrato."))),
    Client("cli_22", "Daniela Barbosa", "Inativo", "Ex-Clientes", "Há 2 meses", 1, listOf("Concorrência"), interactions = listOf(Interaction(type = "Anotação", content = "Migrou para concorrente."))),
    Client("cli_23", "Leonardo Almeida", "Ativo", "Clientes VIP", "Hoje", 5, listOf("VIP", "Engajado"), interactions = emptyList()),
    Client("cli_24", "Sofia Carvalho", "Cliente em Risco", "Alto Risco", "Há 3d", 2, listOf("Pouco Uso"), interactions = listOf(Interaction(type = "Anotação", content = "Não acessa a plataforma."))),
    Client("cli_25", "Bruno Pinto", "Novo", "Novos Clientes", "Há 5min", 4, listOf("Interessado"), interactions = listOf(Interaction(type = "Anotação", content = "Ligou para tirar dúvidas."))),
    Client("cli_26", "Letícia Dias", "Ativo", "Clientes Padrão", "Ontem", 3, listOf(), interactions = emptyList()),
    Client("cli_27", "Marcos Teixeira", "Ativo", "Em Negociação", "Há 9h", 3, listOf("Proposta Enviada"), interactions = emptyList()),
    Client("cli_28", "Gabriela Gomes", "Ativo", "Clientes VIP", "Há 1h", 5, listOf("Fidelizado"), interactions = emptyList()),
    Client("cli_29", "Rodrigo Justino", "Cliente em Risco", "Alto Risco", "Há 6d", 2, listOf("Reclamacao"), interactions = emptyList()),
    Client("cli_30", "Helena Furtado", "Novo", "Novos Clientes", "Há 10min", 4, listOf("Onboarding"), interactions = listOf(Interaction(type = "Anotação", content = "Acabou de se cadastrar.")))
)