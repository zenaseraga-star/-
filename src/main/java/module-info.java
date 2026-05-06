module ru.itmo.ArsikAndEva {
    requires javafx.controls;
    requires javafx.graphics;
    exports ru.itmo.ArsikAndEva.ui;
    exports ru.itmo.ArsikAndEva.manager;
    exports ru.itmo.ArsikAndEva.model;
    exports ru.itmo.ArsikAndEva.validator;
    exports ru.itmo.ArsikAndEva.cli;
    exports ru.itmo.ArsikAndEva.exception;
    exports ru.itmo.ArsikAndEva.storage;
    opens ru.itmo.ArsikAndEva.ui to java.base;
}