package cses;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.lang.reflect.Array;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.csvreader.CsvWriter;
import com.github.javaparser.JavaParser;
import java.io.FileInputStream;

import com.github.javaparser.TokenRange;
import com.github.javaparser.ast.CompilationUnit;
import com.github.javaparser.ast.PackageDeclaration;
import com.github.javaparser.ast.body.ClassOrInterfaceDeclaration;
import com.github.javaparser.ast.body.MethodDeclaration;
import com.github.javaparser.ast.comments.Comment;
import com.github.javaparser.ast.comments.LineComment;
import com.github.javaparser.ast.stmt.BlockStmt;
import com.github.javaparser.ast.visitor.VoidVisitorAdapter;
import java.util.Optional;

public class AstParser {
    private static class MethodVisitor extends VoidVisitorAdapter<Void> {
        int i;
        int j;
        public MethodVisitor(int ii) {
            i = ii;
            j = 0;
        }
        @Override
        public void visit(MethodDeclaration n, Void arg) {
            try {
                RandomAccessFile randomFile = new RandomAccessFile("code.csv", "rw");
                long fileLength = randomFile.length();
                randomFile.seek(fileLength);
                Optional<Comment> c = n.getComment();
                String cc = "";
                if (c.isPresent()){
                    cc = c.get().getContent().replace("\n","").replace("  ","").replace("*","");
                }
                String body = "";
                Optional<TokenRange> b = n.getTokenRange();
                if (b.isPresent()){
                    body = b.get().toString().replace("\n","").replace("  ","");
                }
                String content = String.valueOf(i)+"$"
                        +String.valueOf(j++)+"$"
                        +body+"$"
                        +cc;
                randomFile.writeBytes(content+"\r\n");
                randomFile.close();
            } catch (Exception e) {
                e.printStackTrace();
            }
            super.visit(n, arg);
        }
    }
    public static void main(String args[]) {
        try {
            String pathToAppSourceCode = "/home/yansh/project/project/datasetForCSES/data/origin1010/";//args[0];
            File file = new File(pathToAppSourceCode);
            String[] filelist = file.list();
            for (int i = 0; i < filelist.length; i++) {
                FileInputStream in = new FileInputStream(pathToAppSourceCode+filelist[i]);
                CompilationUnit cu = JavaParser.parse(in);
                cu.accept(new MethodVisitor(i), null);
                in.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


}

