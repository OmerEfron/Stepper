package Stepper.Logic.ReadStepper.api.Reader;

import Stepper.Logic.ReadStepper.TheStepper;
import generated.STStepper;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

public class StepperReaderFromXml implements StepperReader {

    TheStepper theStepper;
    @Override
    public TheStepper read(String filePath) {
        try {
            InputStream inputStream =  new FileInputStream(new File(filePath));
            JAXBContext jaxbContext = JAXBContext.newInstance("generated");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            STStepper stStepper = (STStepper) unmarshaller.unmarshal(inputStream);
            return new TheStepper(stStepper);
        } catch (IOException e) {
            throw new RuntimeException(e);
        } catch (JAXBException e) {
            throw new RuntimeException(e);
        }
    }
}
