const ticketsContainer = document.getElementById("ticketsContainer");

function formatDate(dateString) {
    if (!dateString) return "Dato non disponibile";

    const date = new Date(dateString);
    return date.toLocaleDateString("it-IT") + " " + date.toLocaleTimeString("it-IT", {
        hour: "2-digit",
        minute: "2-digit"
    });
}

function getStatus(ticket) {
    if (!ticket.valid) return "NON VALIDO";
    if (ticket.checkedIn) return "USATO";
    return "ATTIVO";
}

function createTicketCard(ticket) {
    const status = getStatus(ticket);

    return `
        <div class="ticket-card">
            <div class="ticket-left">
                <div class="ticket-header">
                    <div class="ticket-title">${ticket.eventName ?? "Evento"}</div>
                    <div class="ticket-status">${status}</div>
                </div>

                <div class="ticket-details">

                    <!-- RIGA 1 -->
                    <div class="ticket-detail">
                        <div class="ticket-detail-label">Intestatario</div>
                        <div class="ticket-detail-value">
                            ${[ticket.firstName, ticket.lastName].filter(Boolean).join(" ") || "Dato non disponibile"}
                        </div>
                    </div>

                    <div class="ticket-detail">
                        <div class="ticket-detail-label">Email acquisto</div>
                        <div class="ticket-detail-value">
                            ${ticket.email ?? "Dato non disponibile"}
                        </div>
                    </div>

                    <!-- RIGA 2 -->
                    <div class="ticket-detail">
                        <div class="ticket-detail-label">Data evento</div>
                        <div class="ticket-detail-value">
                            ${formatDate(ticket.eventDate)}
                        </div>
                    </div>

                    <div class="ticket-detail">
                        <div class="ticket-detail-label">Luogo</div>
                        <div class="ticket-detail-value">
                            ${ticket.eventLocation ?? "Dato non disponibile"}
                        </div>
                    </div>

                    <!-- RIGA 3 -->
                    <div style="grid-column: 1 / -1; display: grid; grid-template-columns: repeat(3, minmax(0, 1fr)); gap: 15px;">

                        <div class="ticket-detail" style="min-width: 0;">
                            <div class="ticket-detail-label">Tipo biglietto</div>
                            <div class="ticket-detail-value">
                                ${ticket.ticketType ?? "Dato non disponibile"}
                            </div>
                        </div>

                        <div class="ticket-detail" style="min-width: 0;">
                            <div class="ticket-detail-label">Costo</div>
                            <div class="ticket-detail-value">
                                ${ticket.price != null ? ticket.price + " €" : "Dato non disponibile"}
                            </div>
                        </div>

                        <div class="ticket-detail" style="min-width: 0;">
                            <div class="ticket-detail-label">Data acquisto</div>
                            <div class="ticket-detail-value">
                                ${formatDate(ticket.purchaseDate)}
                            </div>
                        </div>

                    </div>
                </div>
            </div>

            <div class="ticket-right">
                <div class="qr-box">
                    <img src="https://api.qrserver.com/v1/create-qr-code/?size=150x150&data=${encodeURIComponent(ticket.qrCode)}" alt="QR Code">
                </div>
                <div class="ticket-code">${ticket.qrCode ?? ""}</div>
            </div>
        </div>
    `;
}

async function loadMyTickets() {
    const token = localStorage.getItem("token");

    try {
        const response = await fetch("/tickets/my", {
            method: "GET",
            headers: {
                "Authorization": `Bearer ${token}`
            }
        });

        if (!response.ok) {
            throw new Error("Errore nel recupero dei biglietti");
        }

        const tickets = await response.json();

        if (!tickets.length) {
            ticketsContainer.innerHTML = `
                <div class="ticket-card">
                    <div class="ticket-left">
                        <div class="ticket-title">Nessun biglietto acquistato</div>
                    </div>
                </div>
            `;
            return;
        }

        ticketsContainer.innerHTML = tickets.map(createTicketCard).join("");
    } catch (error) {
        ticketsContainer.innerHTML = `
            <div class="ticket-card">
                <div class="ticket-left">
                    <div class="ticket-title">Impossibile caricare i biglietti</div>
                </div>
            </div>
        `;
        console.error(error);
    }
}

loadMyTickets();