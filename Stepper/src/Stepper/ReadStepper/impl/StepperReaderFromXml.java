package Stepper.ReadStepper.impl;

import Stepper.ReadStepper.XMLReadClasses.TheStepper;
import Stepper.ReadStepper.Exception.ReadException;
import Stepper.ReadStepper.api.StepperReader;
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
    public TheStepper read(String filePath) throws ReadException {
        try {
            InputStream inputStream =  new FileInputStream(new File(filePath));
            JAXBContext jaxbContext = JAXBContext.newInstance("generated");
            Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();
            STStepper stStepper = (STStepper) unmarshaller.unmarshal(inputStream);
            return new TheStepper(stStepper);
        } catch (IOException | JAXBException e) {
            throw new ReadException("cannot read file", filePath);
        }
    }
}
