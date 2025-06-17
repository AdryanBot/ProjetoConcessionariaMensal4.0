package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.function.Function;

public class TabelaUtils {

    public static <T> JScrollPane gerarTabela(String[] colunas, List<T> dados, Function<T, Object[]> mapeadorLinha) {
        // Subclasse anônima do DefaultTableModel que impede edição
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Impede edição de qualquer célula
            }
        };

        for (T item : dados) {
            modelo.addRow(mapeadorLinha.apply(item));
        }

        JTable tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);
        return scrollPane;
    }
}
