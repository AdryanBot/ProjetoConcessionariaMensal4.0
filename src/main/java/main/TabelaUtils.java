package main;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.function.Function;

public class TabelaUtils {

    public static <T> JScrollPane gerarTabela(String[] colunas, List<T> dados, Function<T, Object[]> mapeadorLinha) {
        DefaultTableModel modelo = new DefaultTableModel(colunas, 0);
        for (T item : dados) {
            modelo.addRow(mapeadorLinha.apply(item));
        }

        JTable tabela = new JTable(modelo);
        JScrollPane scrollPane = new JScrollPane(tabela);
        return scrollPane;
    }
}
