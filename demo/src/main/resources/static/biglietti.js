const tickets = [
    {
        evento: "Concerto Rock",
        data: "10/05/2026",
        ora: "21:00",
        luogo: "Palazzetto dello Sport, Roma",
        posto: "Settore A - Fila 3 - Posto 12",
        costo: "45,00 €",
        codice: "ROCK-ROMA-2026-A3-12",
        stato: "ATTIVO",
        qr: "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=ROCK-ROMA-2026-A3-12"
    },
    {
        evento: "Spettacolo Teatro",
        data: "15/05/2026",
        ora: "20:30",
        luogo: "Teatro Centrale, Milano",
        posto: "Platea - Fila 1 - Posto 8",
        costo: "32,50 €",
        codice: "TEATRO-MI-2026-P1-08",
        stato: "ATTIVO",
        qr: "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=TEATRO-MI-2026-P1-08"
    },
    {
        evento: "Festival Jazz",
        data: "22/05/2026",
        ora: "19:45",
        luogo: "Arena Summer, Napoli",
        posto: "Settore B - Fila 5 - Posto 21",
        costo: "28,00 €",
        codice: "JAZZ-NA-2026-B5-21",
        stato: "USATO",
        qr: "https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=JAZZ-NA-2026-B5-21"
    }
];

const ticketsContainer = document.getElementById("ticketsContainer");

function createTicketCard(ticket) {
    return `
        <div class="ticket-card">
            <div class="ticket-left">
                <div class="ticket-header">
                    <div class="ticket-title">${ticket.evento}</div>
                    <div class="ticket-status">${ticket.stato}</div>
                </div>

                <div class="ticket-details">
                    <div class="ticket-detail">
                        <div class="ticket-detail-label">Data</div>
                        <div class="ticket-detail-value">${ticket.data}</div>
                    </div>

                    <div class="ticket-detail">
                        <div class="ticket-detail-label">Ora</div>
                        <div class="ticket-detail-value">${ticket.ora}</div>
                    </div>

                    <div class="ticket-detail">
                        <div class="ticket-detail-label">Luogo</div>
                        <div class="ticket-detail-value">${ticket.luogo}</div>
                    </div>

                    <div class="ticket-detail">
                        <div class="ticket-detail-label">Posto</div>
                        <div class="ticket-detail-value">${ticket.posto}</div>
                    </div>

                    <div class="ticket-detail">
                        <div class="ticket-detail-label">Costo</div>
                        <div class="ticket-detail-value">${ticket.costo}</div>
                    </div>

                    <div class="ticket-detail">
                        <div class="ticket-detail-label">Codice biglietto</div>
                        <div class="ticket-detail-value">${ticket.codice}</div>
                    </div>
                </div>
            </div>

            <div class="ticket-right">
                <div class="qr-box">
                    <img src="${ticket.qr}" alt="QR Code">
                </div>
                <div class="ticket-code">${ticket.codice}</div>
            </div>
        </div>
    `;
}

function renderTickets() {
    ticketsContainer.innerHTML = tickets.map(createTicketCard).join("");
}

renderTickets();