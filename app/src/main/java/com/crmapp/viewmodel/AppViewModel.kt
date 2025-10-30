package com.crmapp.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.crmapp.data.mockClientUser
import com.crmapp.data.mockClients
import com.crmapp.data.mockOperator
import com.crmapp.data.models.Client
import com.crmapp.data.models.Interaction
import com.crmapp.data.models.Message
import com.crmapp.data.models.User
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch

enum class CampaignChannel {
    CAMPAIGN, CHAT
}

class AppViewModel : ViewModel() {

    var currentUser by mutableStateOf<User?>(null)
        private set

    private val _clients = mutableStateListOf(*mockClients.toTypedArray())
    val clients: List<Client> = _clients

    var showPopup by mutableStateOf(false)
        private set
    var popupMessage by mutableStateOf("")
        private set

    private val _snackbarEvent = Channel<String>()
    val snackbarEvent = _snackbarEvent.receiveAsFlow()

    private val _messages = mutableStateListOf<Message>()
    val messages: List<Message> = _messages

    init {
        if (mockClients.isNotEmpty()) {
            _messages.addAll(
                listOf(
                    Message(senderId = mockOperator.id, content = "Olá Maria, recebi sua solicitação.", chatId = mockClients.first().id, isGroup = false),
                    Message(senderId = mockClients.first().id, content = "Gostaria de saber sobre as condições do plano VIP.", chatId = mockClients.first().id, isGroup = false),
                    Message(senderId = mockOperator.id, content = "Claro! As condições atuais são excelentes.", chatId = mockClients.first().id, isGroup = false)
                )
            )
        }
        simulateNewMessageArrival()
    }

    private fun simulateNewMessageArrival() {
        viewModelScope.launch {
            delay(5000)
            popupMessage = "Nova mensagem de Maria Silva no Chat 1:1!"
            showPopup = true
            delay(3000)
            showPopup = false
        }
    }

    fun showSnackbar(message: String) {
        viewModelScope.launch {
            _snackbarEvent.send(message)
        }
    }

    fun login(isOperatorLogin: Boolean) {
        currentUser = if (isOperatorLogin) mockOperator else mockClientUser
    }

    fun logout() {
        currentUser = null
    }

    fun addInteraction(clientId: String, type: String, content: String) {
        val clientIndex = _clients.indexOfFirst { it.id == clientId }
        if (clientIndex != -1) {
            val oldClient = _clients[clientIndex]
            val newInteraction = Interaction(type = type, content = content)
            val updatedInteractions = oldClient.interactions + newInteraction
            _clients[clientIndex] = oldClient.copy(interactions = updatedInteractions)
        }
    }

    fun sendMessage(content: String, targetId: String, isGroup: Boolean) {
        if (content.isNotBlank()) {
            addInteraction(targetId, "Chat", content)
            _messages.add(
                Message(
                    senderId = currentUser!!.id,
                    content = content,
                    chatId = targetId,
                    isGroup = isGroup
                )
            )
        }
    }

    fun sendMassMessage(recipients: List<Client>, message: String, channel: CampaignChannel) {
        viewModelScope.launch {
            recipients.forEach { client ->
                when (channel) {
                    CampaignChannel.CAMPAIGN -> {
                        addInteraction(client.id, "Campanha", message)
                    }
                    CampaignChannel.CHAT -> {
                        addInteraction(client.id, "Chat em Massa", message)
                        _messages.add(
                            Message(
                                senderId = currentUser!!.id,
                                content = message,
                                chatId = client.id,
                                isGroup = false
                            )
                        )
                    }
                }
            }
        }
    }
}