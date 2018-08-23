package charge_your_vehicle.model.entity.charging_points_data;

import java.util.List;

public class Level {

    private int id;
    private String title;
    private String comments;
    private List<Connection> connections;
    private boolean isFastChargeCapable;

    public Level() {
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public boolean isFastChargeCapable() {
        return isFastChargeCapable;
    }

    public void setFastChargeCapable(boolean fastChargeCapable) {
        isFastChargeCapable = fastChargeCapable;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<Connection> getConnections() {
        return connections;
    }

    public void setConnections(List<Connection> connections) {
        this.connections = connections;
    }
}
