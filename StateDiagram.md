```mermaid


stateDiagram-v2
    [*] --> Available : System Start

    state Available {
        [*]-->Idle
        Idle --> Idle : Filter by City/Type
    }

    Available --> Rented : Renting Process
    Rented --> Available : Process Return

    Rented --> Unavailable : Send to Maintenance (Crash)
    Available --> Unavailable : Send to Maintenance (Check)

    state Unavailable {
        [*]-->InMaintenance
        InMaintenance --> ResolveDialog : Open Resolve
        ResolveDialog --> SaveHistory : Add Notes
    }

    Unavailable --> Available : Resolved
    Available --> [*] : System Shutdown