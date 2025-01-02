package API.EventTom.models;

import lombok.Getter;

@Getter
public enum Roles {
    EVENT_MANAGER("Event Manager"),
    EVENT_CREATOR("Event Creator"),
    SALES_REP("Sales Representative"),
    ADMINISTRATOR("Administrator"),

    NONE("No Role");

    private final String displayName;

    Roles(String displayName) {
        this.displayName = displayName;
    }

    public String getAuthority() {
        return this.name();
    }
}