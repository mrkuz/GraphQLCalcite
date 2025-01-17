//import com.google.common.collect.ImmutableList;
//import com.google.common.collect.ImmutableMap;
//import org.apache.calcite.DataContext;
//import org.apache.calcite.linq4j.Enumerable;
//import org.apache.calcite.linq4j.Linq4j;
//import org.apache.calcite.rel.RelCollations;
//import org.apache.calcite.rel.RelFieldCollation;
//import org.apache.calcite.rel.type.RelDataType;
//import org.apache.calcite.rel.type.RelDataTypeFactory;
//import org.apache.calcite.schema.ScannableTable;
//import org.apache.calcite.schema.Statistic;
//import org.apache.calcite.schema.Statistics;
//import org.apache.calcite.schema.Table;
//import org.apache.calcite.schema.impl.AbstractSchema;
//import org.apache.calcite.schema.impl.AbstractTable;
//import org.apache.calcite.util.ImmutableBitSet;
//import org.checkerframework.checker.nullness.qual.Nullable;
//
//import java.util.ArrayList;
//import java.util.Arrays;
//import java.util.List;
//import java.util.Map;
//import java.util.function.Function;
//
///**
// * A typical HR schema with employees (emps) and departments (depts) tables that are naturally
// * ordered based on their primary keys representing clustered tables.
// */
//public final class HrClusteredSchema extends AbstractSchema {
//
//    private final ImmutableMap<String, Table> tables;
//
//    public HrClusteredSchema() {
//        tables = ImmutableMap.<String, Table>builder()
//                .put("emps",
//                        new PkClusteredTable(
//                                factory ->
//                                        new RelDataTypeFactory.Builder(factory)
//                                                .add("empid", factory.createJavaType(int.class))
//                                                .add("deptno", factory.createJavaType(int.class))
//                                                .add("name", factory.createJavaType(String.class))
//                                                .add("salary", factory.createJavaType(int.class))
//                                                .add("commission", factory.createJavaType(Integer.class))
//                                                .build(),
//                                ImmutableBitSet.of(0),
//                                Arrays.asList(
//                                        new Object[]{100, 10, "Bill", 10000, 1000},
//                                        new Object[]{110, 10, "Theodore", 11500, 250},
//                                        new Object[]{150, 10, "Sebastian", 7000, null},
//                                        new Object[]{200, 20, "Eric", 8000, 500})))
//                .put("depts",
//                        new PkClusteredTable(
//                                factory ->
//                                        new RelDataTypeFactory.Builder(factory)
//                                                .add("deptno", factory.createJavaType(int.class))
//                                                .add("name", factory.createJavaType(String.class))
//                                                .build(),
//                                ImmutableBitSet.of(0),
//                                Arrays.asList(
//                                        new Object[]{10, "Sales"},
//                                        new Object[]{30, "Marketing"},
//                                        new Object[]{40, "HR"})))
//                .build();
//    }
//
//    @Override
//    protected Map<String, Table> getTableMap() {
//        return tables;
//    }
//
//    /**
//     * A table sorted (ascending direction and nulls last) on the primary key.
//     */
//    private static class PkClusteredTable extends AbstractTable implements ScannableTable {
//        private final ImmutableBitSet pkColumns;
//        private final List<Object[]> data;
//        private final Function<RelDataTypeFactory, RelDataType> typeBuilder;
//
//        PkClusteredTable(
//                Function<RelDataTypeFactory, RelDataType> dataTypeBuilder,
//                ImmutableBitSet pkColumns,
//                List<Object[]> data) {
//            this.data = data;
//            this.typeBuilder = dataTypeBuilder;
//            this.pkColumns = pkColumns;
//        }
//
//        @Override
//        public Statistic getStatistic() {
//            List<RelFieldCollation> collationFields = new ArrayList<>();
//            for (Integer key : pkColumns) {
//                collationFields.add(
//                        new RelFieldCollation(
//                                key,
//                                RelFieldCollation.Direction.ASCENDING,
//                                RelFieldCollation.NullDirection.LAST));
//            }
//            return Statistics.of(data.size(), ImmutableList.of(pkColumns),
//                    ImmutableList.of(RelCollations.of(collationFields)));
//        }
//
//        @Override
//        public RelDataType getRowType(final RelDataTypeFactory typeFactory) {
//            return typeBuilder.apply(typeFactory);
//        }
//
//        @Override
//        public Enumerable<@Nullable Object[]> scan(final DataContext root) {
//            return Linq4j.asEnumerable(data);
//        }
//
//    }
//}