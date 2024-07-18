package a2_2001040208.model;

import a2_2001040208.common.PatronType;

import java.util.Date;
import java.util.Objects;

// A library patron
public class Patron {
    // supportive attributes
    private static int PATRON_NO = 0;
    private final String patronID;
    private final String name;
    private final Date dob;
    private final String email;
    private final String phoneNo;
    private final PatronType patronType;

    public Patron(String name, Date dob, String email, String phoneNo, PatronType patronType) {
        if (!validateName(name)) {
            throw new IllegalArgumentException("Patron.init: Invalid name!");
        } else if (!validateDob(dob)) {
            throw new IllegalArgumentException("Patron.init: Invalid date of birth!");
        } else if (!validateEmail(email)) {
            throw new IllegalArgumentException("Patron.init: Invalid email!");
        } else if (!validatePhoneNo(phoneNo)) {
            throw new IllegalArgumentException("Patron.init: Invalid phone number!");
        } else if (!validatePatronType(patronType)) {
            throw new IllegalArgumentException("Patron.init: Invalid patron type!");
        } else {
            PATRON_NO++;
            this.patronID = generatePatronID();

            this.name = name;
            this.dob = dob;
            this.email = email;
            this.phoneNo = phoneNo;
            this.patronType = patronType;
        }
    }

    // TODO 2: add new constructor
    public Patron(String patronID, String name, Date dob, String email, String phoneNo, PatronType patronType) {
        if (!validateName(name)) {
            throw new IllegalArgumentException("Patron.init: Invalid name!");
        } else if (!validateDob(dob)) {
            throw new IllegalArgumentException("Patron.init: Invalid date of birth!");
        } else if (!validateEmail(email)) {
            throw new IllegalArgumentException("Patron.init: Invalid email!");
        } else if (!validatePhoneNo(phoneNo)) {
            throw new IllegalArgumentException("Patron.init: Invalid phone number!");
        } else if (!validatePatronType(patronType)) {
            throw new IllegalArgumentException("Patron.init: Invalid patron type!");
        } else {
            try {
                PATRON_NO = Integer.parseInt(patronID);
                this.patronID = generatePatronID();
                this.name = name;
                this.dob = dob;
                this.email = email;
                this.phoneNo = phoneNo;
                this.patronType = patronType;
            } catch (NumberFormatException e) {
                throw new IllegalArgumentException("Patron.init: Invalid ID format!");
            }
        }
    }

    // TODO 2: add all missing getters

    public String getPatronID() {
        return patronID;
    }

    // TODO 2: get integer ID
    public int getRawID() {
        if (patronID.contains("0")) {
            // get number after P00
            return Integer.parseInt(patronID.substring(patronID.lastIndexOf("0") + 1));
        } else {
            // get number after P
            return Integer.parseInt(patronID.substring(1));
        }
    }

    public String getName() {
        return name;
    }

    public Date getDob() {
        return dob;
    }

    public String getEmail() {
        return email;
    }

    public String getPhoneNo() {
        return phoneNo;
    }

    public PatronType getPatronType() {
        return patronType;
    }

    private boolean validateName(String name) {
        if (name == null || name.length() < 2) return false;
        // perhaps the patron name should not contain numbers...?
        for (char c : name.toCharArray()) {
            if (Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean validateDob(Date dob) {
        return dob != null;
    }

    private boolean validateEmail(String email) {
        // min of 3 chars: A@B
        return email != null && email.length() >= 3 && email.contains("@");
    }

    private boolean validatePhoneNo(String phoneNo) {
        if (phoneNo == null || phoneNo.length() == 0) return false;
        for (char c : phoneNo.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    private boolean validatePatronType(PatronType patronType) {
        return patronType != null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Patron patron = (Patron) o;
        return Objects.equals(patronID, patron.patronID) && Objects.equals(name, patron.name) && Objects.equals(dob, patron.dob) && Objects.equals(email, patron.email) && Objects.equals(phoneNo, patron.phoneNo) && patronType == patron.patronType;
    }

    // TODO 2: toString()
    @Override
    public String toString() {
        return String.format("%-5s|  %s,  tel: %s", patronID, name, phoneNo);
    }

    // TODO:  generatePatronID()
    //      Generate a unique ID for each Patron with number auto-incremented from 1
    //      e.g. P001, ..., P100, ...
    public String generatePatronID() {
        if (PATRON_NO < 10) {
            return "P00" + PATRON_NO;
        } else if (PATRON_NO < 100) {
            return "P0" + PATRON_NO;
        } else {
            return "P" + PATRON_NO;
        }
    }
}
