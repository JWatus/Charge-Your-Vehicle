package charge_your_vehicle.model.entity.charging_points_data;

public class OperatorInfo {

    private int id;
    private String title;
    private String websiteURL;
    private String comments;
    private AddressInfo addressInfo;
    private String bookingURL;
    private String contactEmail;
    private String faultReportEmail;

    public OperatorInfo() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWebsiteURL() {
        return websiteURL;
    }

    public void setWebsiteURL(String websiteURL) {
        this.websiteURL = websiteURL;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public AddressInfo getAddressInfo() {
        return addressInfo;
    }

    public void setAddressInfo(AddressInfo addressInfo) {
        this.addressInfo = addressInfo;
    }

    public String getBookingURL() {
        return bookingURL;
    }

    public void setBookingURL(String bookingURL) {
        this.bookingURL = bookingURL;
    }

    public String getContactEmail() {
        return contactEmail;
    }

    public void setContactEmail(String contactEmail) {
        this.contactEmail = contactEmail;
    }

    public String getFaultReportEmail() {
        return faultReportEmail;
    }

    public void setFaultReportEmail(String faultReportEmail) {
        this.faultReportEmail = faultReportEmail;
    }
}
