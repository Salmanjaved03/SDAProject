package application;

public class VenueManager {

    private static VenueManager instance;

    private Venue selectedVenue;

    private VenueManager() {}

    public static VenueManager getInstance() {
        if (instance == null) {
            instance = new VenueManager();
        }
        return instance;
    }

   
    public void setSelectedVenue(Venue venue) {
        this.selectedVenue = venue;
    }

    public Venue getSelectedVenue() {
        return selectedVenue;
    }

    public void clearSelectedVenue() {
        this.selectedVenue = null;
    }
}
