package charge_your_vehicle.model.entity.charging_points_data;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "CHARGING_POINTS")
public class ChargingPoint {

    @Id
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "address_id")
    private AddressInfo addressInfo;

    @Transient
    private List<Connection> connectionList;
    @Transient
    private String uuid;
    @Transient
    private int parentChargePointID;
    @Transient
    private OperatorInfo operatorInfo;
    @Transient
    private UsageType usageType;
    @Transient
    private String usageCost;
    @Transient
    private StatusType statusType;
    @Transient
    private Date dateLastStatusUpdate;
    @Transient
    private int dataQualityLevel;
    @Transient
    private Date dateCreated;
    @Transient
    private Date dateLastVerified;
    @Transient
    private int numberOfPoints;
    @Transient
    private String generalComments;
    @Transient
    private Date datePlanned;
    @Transient
    private Date dateLastConfirmed;

    public ChargingPoint() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getUuid() {
        return uuid;
    }

    public void setUuid(String uuid) {
        this.uuid = uuid;
    }

    public int getParentChargePointID() {
        return parentChargePointID;
    }

    public void setParentChargePointID(int parentChargePointID) {
        this.parentChargePointID = parentChargePointID;
    }

    public OperatorInfo getOperatorInfo() {
        return operatorInfo;
    }

    public void setOperatorInfo(OperatorInfo operatorInfo) {
        this.operatorInfo = operatorInfo;
    }

    public UsageType getUsageType() {
        return usageType;
    }

    public void setUsageType(UsageType usageType) {
        this.usageType = usageType;
    }

    public String getUsageCost() {
        return usageCost;
    }

    public void setUsageCost(String usageCost) {
        this.usageCost = usageCost;
    }

    public StatusType getStatusType() {
        return statusType;
    }

    public void setStatusType(StatusType statusType) {
        this.statusType = statusType;
    }

    public Date getDateLastStatusUpdate() {
        return dateLastStatusUpdate;
    }

    public void setDateLastStatusUpdate(Date dateLastStatusUpdate) {
        this.dateLastStatusUpdate = dateLastStatusUpdate;
    }

    public int getDataQualityLevel() {
        return dataQualityLevel;
    }

    public void setDataQualityLevel(int dataQualityLevel) {
        this.dataQualityLevel = dataQualityLevel;
    }

    public Date getDateCreated() {
        return dateCreated;
    }

    public void setDateCreated(Date dateCreated) {
        this.dateCreated = dateCreated;
    }

    public List<Connection> getConnectionList() {
        return connectionList;
    }

    public void setConnectionList(List<Connection> connectionList) {
        this.connectionList = connectionList;
    }

    public Date getDateLastVerified() {
        return dateLastVerified;
    }

    public void setDateLastVerified(Date dateLastVerified) {
        this.dateLastVerified = dateLastVerified;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public int getNumberOfPoints() {
        return numberOfPoints;
    }

    public void setNumberOfPoints(int numberOfPoints) {
        this.numberOfPoints = numberOfPoints;
    }

    public String getGeneralComments() {
        return generalComments;
    }

    public void setGeneralComments(String generalComments) {
        this.generalComments = generalComments;
    }

    public Date getDatePlanned() {
        return datePlanned;
    }

    public void setDatePlanned(Date datePlanned) {
        this.datePlanned = datePlanned;
    }

    public Date getDateLastConfirmed() {
        return dateLastConfirmed;
    }

    public void setDateLastConfirmed(Date dateLastConfirmed) {
        this.dateLastConfirmed = dateLastConfirmed;
    }
}
