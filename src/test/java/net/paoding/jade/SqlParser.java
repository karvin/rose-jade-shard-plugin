package net.paoding.jade;

import net.sf.jsqlparser.JSQLParserException;
import net.sf.jsqlparser.parser.CCJSqlParserManager;
import net.sf.jsqlparser.parser.JSqlParser;
import net.sf.jsqlparser.schema.Table;
import net.sf.jsqlparser.statement.Statement;
import net.sf.jsqlparser.statement.select.*;

import java.io.StringReader;

/**
 * Created by karvin on 16/1/29.
 */
public class SqlParser {

    private static JSqlParser sqlParser = new CCJSqlParserManager();

    public void testSelectSql(){
        try {
            Statement statement = sqlParser.parse(new StringReader("select * from user where id=?"));
            Select select = (Select)statement;
            SelectBody selectBody = select.getSelectBody();
            if(selectBody instanceof PlainSelect){
                PlainSelect plainSelect = (PlainSelect)selectBody;
                FromItem fromItem = plainSelect.getFromItem();
                if(fromItem instanceof Table){
                    Table table = (Table)fromItem;
                    System.out.println(table);
                    table.setName("table_1");
                }
                System.out.println(statement.toString());
            }else if(selectBody instanceof Union){
                Union union = (Union)selectBody;
            }
        } catch (JSQLParserException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args){
        SqlParser parser = new SqlParser();
        parser.testSelectSql();
    }
}
