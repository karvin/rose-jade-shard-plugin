package net.paoding.rose.jade.shard.parser;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.expression.BinaryExpression;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.operators.relational.InExpression;
import net.sf.jsqlparser.expression.operators.relational.ItemsList;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.JSqlParser;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.create.table.CreateTable;
import net.sf.jsqlparser.statement.delete.Delete;
import net.sf.jsqlparser.statement.insert.Insert;
import net.sf.jsqlparser.statement.replace.Replace;
import net.sf.jsqlparser.statement.select.*;
import net.sf.jsqlparser.statement.truncate.Truncate;
import net.sf.jsqlparser.statement.update.Update;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by karvin on 16/1/29.
 * CCJSqlParserManager is not thread safe,so i have to create a new parser for each sql
 */
public class SqlParser {

    public static String generate(String sql,String oldName,String newTableName) throws JSQLParserException {
        JSqlParser parser = new CCJSqlParserManager();
        Statement statement = parser.parse(new StringReader(sql));
        return generate(statement,oldName,newTableName);
    }

    public static Statement parse(String sql) throws JSQLParserException {
        JSqlParser parser = new CCJSqlParserManager();
        return parser.parse(new StringReader(sql));
    }

    public static List<Table> getTables(Statement statement,String oldName){
        List<Table> result = new ArrayList<Table>();
        if(statement instanceof Select){
            Select select = (Select)statement;
            List<Table> tables = getReadTables(select.getSelectBody());
            for(Table table:tables){
                if(table.getName().equals(oldName)){
                    result.add(table);
                }
            }
        }else{
            Table table = getWriteTable(statement);
            if(table.getName().equals(oldName)){
                result.add(table);
            }
        }
        return result;
    }

    private static String generate(Statement statement,String oldName,String newTableName){
        if(statement instanceof Select){
            Select select = (Select)statement;
            List<Table> tables = getReadTables(select.getSelectBody());
            if(tables != null && tables.size()>0){
                for(Table t:tables){
                    if(t != null && t.getName().equals(oldName)){
                        t.setName(newTableName);
                    }
                }
            }
        }else{
            Table table = getWriteTable(statement);
            if(table != null && table.getName().equals(oldName)){
                table.setName(newTableName);
            }
        }
        return statement.toString();
    }

    private static Table getWriteTable(Statement statement){
        Table table = null;
        if(statement instanceof Update){
            Update update = (Update)statement;
            table = update.getTable();
        }else if(statement instanceof Insert){
            Insert insert = (Insert)statement;
            table = insert.getTable();
        }else if(statement instanceof Delete){
            Delete delete = (Delete)statement;
            table = delete.getTable();
        }else if(statement instanceof CreateTable){
            CreateTable createTable = (CreateTable)statement;
            table = createTable.getTable();
        }else if(statement instanceof Replace){
            Replace replace = (Replace)statement;
            table = replace.getTable();
        }else if(statement instanceof Truncate){
            Truncate truncate = (Truncate)statement;
            table = truncate.getTable();
        }
        return table;
    }

    private static List<Table> getReadTables(SelectBody selectBody){
        List<Table> tables = new ArrayList<Table>();
        if(selectBody instanceof PlainSelect){
            PlainSelect plainSelect = (PlainSelect)selectBody;
            return getPlainSelectTable(plainSelect);
        }else if(selectBody instanceof Union){
            Union union = (Union)selectBody;
            List<PlainSelect> list = union.getPlainSelects();
            for(PlainSelect plainSelect: list){
                tables.addAll(getPlainSelectTable(plainSelect));
            }
        }
        return tables;
    }

    private static List<Table> getPlainSelectTable(PlainSelect plainSelect){
        List<Table> tables = new ArrayList<Table>();
        FromItem fromItem = plainSelect.getFromItem();
        tables.addAll(getFromItemTable(fromItem));
        Expression expression = plainSelect.getWhere();
        if(expression instanceof BinaryExpression){
            BinaryExpression binaryExpression = (BinaryExpression)expression;
            Expression left = binaryExpression.getLeftExpression();
            if(left instanceof SubSelect){
                tables.addAll(getFromItemTable((SubSelect)left));
            }
            Expression right = binaryExpression.getRightExpression();
            if(right instanceof SubSelect) {
                SubSelect subSelect = (SubSelect) right;
                tables.addAll(getFromItemTable(subSelect));
            }
        }else if(expression instanceof InExpression){
            InExpression inExpression = (InExpression)expression;
            ItemsList itemsList = inExpression.getItemsList();
            if(itemsList instanceof SubSelect){
                tables.addAll(getFromItemTable((SubSelect)itemsList));
            }
        }
        return tables;
    }

    private static List<Table> getFromItemTable(FromItem fromItem){
        List<Table> tables =  new ArrayList<Table>();
        if(fromItem instanceof Table){
            tables.add((Table)fromItem);
        }else if(fromItem instanceof SubJoin){
            SubJoin subJoin = (SubJoin)fromItem;
            FromItem joinLeft = subJoin.getLeft();
            tables.addAll(getFromItemTable(joinLeft));
            Join join = subJoin.getJoin();
            FromItem joinRight = join.getRightItem();
            tables.addAll(getFromItemTable(joinRight));
        }else if(fromItem instanceof SubSelect){
            SubSelect subSelect = (SubSelect)fromItem;
            tables.addAll(getReadTables(subSelect.getSelectBody()));
        }
        return tables;
    }

    public static void main(String[] args){
        String sql = "select * from user where id=?";
        try {
            //System.out.println(SqlParser.generate(sql, "user", "user_1"));
            sql = "select * from user,tab_1 where id=?";
            //System.out.println(SqlParser.generate(sql, "user", "user_2"));
            sql = "select * from user as t left join tab_3 on t.id=tab_3.id where name=? and t.id=?";
            //System.out.println(SqlParser.generate(sql,"user","user_3"));
            sql = "select * from tab_1 where user_id in (select id from user where name=?)";
            System.out.println(SqlParser.generate(sql,"user","user_4"));
            Statement statement = SqlParser.parse(sql);
            List<Table> tables = SqlParser.getTables(statement,"user");
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

}
