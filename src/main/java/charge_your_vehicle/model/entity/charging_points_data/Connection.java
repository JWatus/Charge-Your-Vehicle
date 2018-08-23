package charge_your_vehicle.model.entity.charging_points_data;

public class Connection {

    private int id;
    private Level level;
    private ChargingPoint chargingPoint;
    private ConnectionType connectionType;
    private StatusType statusType;
    private double amps;
    private double voltage;
    private double powerKW;
    private CurrentType currentType;
    private int quantity;
    private String comments;

    public Connection() {
    }

    public ConnectionType getConnectionType() {
        return connectionType;
    }

    public void setConnectionType(ConnectionType connectionType) {
        this.connectionType = connectionType;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public Level getLevel() {
        return level;
    }

    public void setLevel(Level level) {
        this.level = level;
    }

    public double getAmps() {
        return amps;
    }

    public void setAmps(double amps) {
        this.amps = amps;
    }

    public double getVoltage() {
        return voltage;
    }

    public void setVoltage(double voltage) {
        this.voltage = voltage;
    }

    public double getPowerKW() {
        return powerKW;
    }

    public void setPowerKW(double powerKW) {
        this.powerKW = powerKW;
    }

    public CurrentType getCurrentType() {
        return currentType;
    }

    public void setCurrentType(CurrentType currentType) {
        this.currentType = currentType;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ChargingPoint getChargingPoint() {
        return chargingPoint;
    }

    public void setChargingPoint(ChargingPoint chargingPoint) {
        this.chargingPoint = chargingPoint;
    }
}
