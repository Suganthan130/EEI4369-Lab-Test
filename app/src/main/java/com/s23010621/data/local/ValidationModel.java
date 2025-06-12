package com.s23010621.data.local;

public class ValidationModel {
    private String messageError;
    private boolean isValid;

    public ValidationModel(String messageError, boolean isValid) {
        this.messageError = messageError;
        this.isValid = isValid;
    }

    public String getMessageError() {
        return messageError;
    }

    public void setMessageError(String messageError) {
        this.messageError = messageError;
    }

    public boolean isValid() {
        return isValid;
    }

    public void setValid(boolean valid) {
        isValid = valid;
    }
}
