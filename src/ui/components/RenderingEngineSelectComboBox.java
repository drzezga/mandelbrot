package ui.components;

import threads.ThreadType;

import javax.swing.*;
import java.util.ArrayList;
import java.util.HashMap;

public class RenderingEngineSelectComboBox extends JPanel {

    public HashMap<String, ThreadType> threadHashMap = new HashMap<>();

    public JComboBox comboBox;

    public RenderingEngineSelectComboBox() {
        threadHashMap.put("Double Double", ThreadType.DOUBLEDOUBLE);
        threadHashMap.put("Double", ThreadType.DOUBLE);
        threadHashMap.put("Float", ThreadType.FLOAT);
        threadHashMap.put("Palette", ThreadType.PALETTE);

        ArrayList<String> comboBoxValues = new ArrayList<>();

        threadHashMap.forEach((k, v) -> comboBoxValues.add(k));

        comboBox = new JComboBox(comboBoxValues.toArray());

        comboBox.setSelectedItem("Float");

        comboBox.addActionListener(e -> System.out.println(threadHashMap.get(comboBox.getSelectedItem())));

        add(comboBox);
    }
}
