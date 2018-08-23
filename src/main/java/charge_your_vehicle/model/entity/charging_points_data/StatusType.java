package charge_your_vehicle.model.entity.charging_points_data;

public class StatusType {

    private String title;
    private boolean IsOperational;
    private boolean IsUserSelectable;

    public StatusType() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public boolean isOperational() {
        return IsOperational;
    }

    public void setOperational(boolean operational) {
        IsOperational = operational;
    }

    public boolean isUserSelectable() {
        return IsUserSelectable;
    }

    public void setUserSelectable(boolean userSelectable) {
        IsUserSelectable = userSelectable;
    }
}
