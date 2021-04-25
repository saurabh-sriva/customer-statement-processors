package customer.statement.processor.response;

public enum ValidationResultCode {
    SUCCESSFUL, BAD_REQUEST, INCORRECT_END_BALANCE, DUPLICATE_REFERENCE_INCORRECT_END_BALANCE, DUPLICATE_REFERENCE,
    INTERNAL_SERVER_ERROR;

    @Override
    public String toString() {
        return this.name();
    }
}