package nandreas.ordermanagement.dto;

public enum ResponseStatus
{
    SUCCESS,
    FAILED;

    public String toLowerCase()
    {
        return this.toString().toLowerCase();
    }
}
