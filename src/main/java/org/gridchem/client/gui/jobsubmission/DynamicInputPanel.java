package org.gridchem.client.gui.jobsubmission;

import org.apache.airavata.model.application.io.DataType;
import org.apache.airavata.model.application.io.InputDataObjectType;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class DynamicInputPanel extends JPanel {
    private EditJobPanel parent;
    private JPanel innerPanel;
    private List<InputDataObjectType> inputs;
    private List<JTextField> inputFields;

    public DynamicInputPanel (EditJobPanel parent){
        super();
        this.parent = parent;
        setLayout(new FlowLayout(FlowLayout.LEADING));
    }

    public List<InputDataObjectType> getInputs() {
        List<InputDataObjectType> generatedInputs = new ArrayList<>();
        for(int i=0; i<inputs.size(); i++) {
            InputDataObjectType input = inputs.get(i);
            InputDataObjectType genInput = new InputDataObjectType();
            genInput.setName(input.getName());
            genInput.setType(input.getType());//TODO validate inputs
            genInput.setValue(inputFields.get(i).getText());
            generatedInputs.add(genInput);
        }
        return generatedInputs;
    }

    public void draw (List<InputDataObjectType> inputs) {
        this.inputs = inputs;
        inputFields = new ArrayList<>();
        removeAll();
        innerPanel = new JPanel();
        innerPanel.setLayout(new GridLayout(inputs.size(), 2, 10, 10));

        int height =0;
        for (InputDataObjectType input :inputs) {
            if (input.getType().equals(DataType.STRING)) {
                JLabel lbl = new JLabel(input.getName());
                JTextField txtField = new JTextField();
                txtField.setText(input.getValue());
                innerPanel.add(lbl);
                innerPanel.add(txtField);
                inputFields.add(txtField);
                height+=50;
            } else if (input.getType().equals(DataType.FLOAT)) {
                JLabel lbl = new JLabel(input.getName());
                JTextField txtField = new JTextField();
                txtField.setText(input.getValue());
                innerPanel.add(lbl);
                innerPanel.add(txtField);
                inputFields.add(txtField);
                height+=50;
            } else if (input.getType().equals(DataType.INTEGER)) {
                JLabel lbl = new JLabel(input.getName());
                JTextField txtField = new JTextField();
                txtField.setText(input.getValue());
                innerPanel.add(lbl);
                innerPanel.add(txtField);
                inputFields.add(txtField);
                height+=50;
            } else if (input.getType().equals(DataType.URI)) {
                JLabel lbl = new JLabel(input.getName());
                JPanel filePanel = new JPanel();
                filePanel.setLayout(new GridLayout(2,1,5,5));
                final JTextField txtPath = new JTextField(10);
                txtPath.setText(input.getValue());
                txtPath.setSize(60,30);
                txtPath.setMaximumSize(new Dimension());
                JButton selectButton = new JButton("Add");
                final JFileChooser fc = new JFileChooser();

                selectButton.addActionListener(new ActionListener() {
                    @Override
                    public void actionPerformed(ActionEvent e) {
                        int returnVal = fc.showOpenDialog(DynamicInputPanel.this);
                        if (returnVal == JFileChooser.APPROVE_OPTION) {
                            File file = fc.getSelectedFile();
                            System.out.println(file.getAbsolutePath());
                            txtPath.setText(file.getAbsolutePath());
                        }
                    }
                });

                filePanel.add(txtPath);
                filePanel.add(selectButton);
                innerPanel.add(lbl);
                innerPanel.add(filePanel);
                inputFields.add(txtPath);
                height+=80;
            }
        }
        innerPanel.setSize(200, height);
        add(innerPanel);
        repaint();
    }
}
