package ru.itmo.ArsikAndEva.users;

import ru.itmo.ArsikAndEva.cli.Command;

public class ExitUserCommand implements Command {
    private final SessionManager sessionManager;

    public ExitUserCommand(SessionManager sessionManager) {
        this.sessionManager = sessionManager;
    }

    @Override
    public void execute(String[] args) {
        sessionManager.exitUser();
    }
}
