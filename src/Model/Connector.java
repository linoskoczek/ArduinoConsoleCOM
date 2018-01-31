package Model;

import gnu.io.CommPortIdentifier;

import javax.swing.*;
import java.util.Collections;
import java.util.Enumeration;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

class Connector {

    static List<CommPortIdentifier> getPortIdentifiers() {
        Enumeration<CommPortIdentifier> enumComm = CommPortIdentifier.getPortIdentifiers();

        return Collections
                .list(enumComm)
                .stream()
                .filter(c -> c.getPortType() == CommPortIdentifier.PORT_SERIAL)
                .collect(Collectors.toList());
    }

    static CommPortIdentifier getFirstPossiblePort() {
        Optional<CommPortIdentifier> port = getPortIdentifiers().parallelStream().findFirst();
        return port.orElse(null);
    }

    static CommPortIdentifier choosePort() {
        List<CommPortIdentifier> ports = Connector.getPortIdentifiers();

        if (ports.size() == 0) {
            portNotFoundMessage();
            System.exit(2);
            return null;
        } else
            return choosePortFromList(ports);
    }

    private static CommPortIdentifier choosePortFromList(List<CommPortIdentifier> ports) {
        Object[] possibleValues = ports.stream().map(CommPortIdentifier::getName).toArray();
        String selectedValue = (String) JOptionPane.showInputDialog(null,
                "Choose the port you want to use for communication with Arduino:", "Select COM port",
                JOptionPane.INFORMATION_MESSAGE, null,
                possibleValues, possibleValues[0]);
        return ports.stream().filter(p -> p.getName().equals(selectedValue)).findAny().orElse(null);
    }

    private static void portNotFoundMessage() {
        JOptionPane.showMessageDialog(null,
                "Could not find any port available to connect with Arduino!",
                "COM port not found", JOptionPane.ERROR_MESSAGE);
    }
}
