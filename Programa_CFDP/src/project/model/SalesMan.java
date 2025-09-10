package project.model;

/**
 * Representa a un vendedor con tipo y número de documento, nombres y apellidos.
 */
public class SalesMan {
    private String documentType;
    private long documentNumber;
    private String firstName;
    private String lastName;

    public SalesMan(String documentType, long documentNumber, String firstName, String lastName) {
        this.documentType = documentType;
        this.documentNumber = documentNumber;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getDocumentType() {
        return documentType;
    }

    public long getDocumentNumber() {
        return documentNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }
}
