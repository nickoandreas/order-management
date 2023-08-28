package nandreas.ordermanagement.model;

public enum OrderStatus
{
    PENDING,
    NEW,
    PROCESSING,
    COMPLETE,
    FAILED,
    CANCELED;

    public String toLowercase()
    {
        return this.toString().toLowerCase();
    }
}
