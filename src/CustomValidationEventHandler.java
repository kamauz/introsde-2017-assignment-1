import javax.xml.bind.ValidationEvent;
import javax.xml.bind.ValidationEventHandler;
import javax.xml.bind.ValidationEventLocator;

class CustomValidationEventHandler implements ValidationEventHandler {
	CustomValidationEventHandler() {
		
	}
	public boolean handleEvent(ValidationEvent event) {
		if (event.getSeverity() == ValidationEvent.WARNING) {
			return true;
		}
		if ((event.getSeverity() == ValidationEvent.ERROR)
				|| (event.getSeverity() == ValidationEvent.FATAL_ERROR)) {

			System.out.println("Validation Error:" + event.getMessage());

			ValidationEventLocator locator = event.getLocator();
			System.out.println("at line number:" + locator.getLineNumber());
			System.out.println("Unmarshalling Terminated");
			return false;
		}
		return true;
	}

}