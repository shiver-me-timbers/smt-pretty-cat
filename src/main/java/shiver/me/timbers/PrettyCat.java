package shiver.me.timbers;

import shiver.me.timbers.java.JavaColourConfiguration;
import shiver.me.timbers.java.JavaWrappedTransformer;
import shiver.me.timbers.transform.FileTransformer;
import shiver.me.timbers.transform.FileTransformers;
import shiver.me.timbers.transform.Transformers;
import shiver.me.timbers.transform.WrappedTransformer;
import shiver.me.timbers.transform.antlr4.TokenTransformation;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;

import static shiver.me.timbers.ESCAPE.RESET;

/**
 * This application can be used to print different types of source code to the terminal with syntactic highlighting.
 */
public class PrettyCat {

    private static final Transformers<File, TokenTransformation> TRANSFORMATIONS = new FileTransformers(
            new HashMap<String, WrappedTransformer<TokenTransformation>>() {{
                put("java", new JavaWrappedTransformer(new JavaColourConfiguration()));
            }});

    public static void main(String[] args) throws FileNotFoundException {

        // Reset the colour scheme after printing the highlighted source code.
        System.out.println(new FileTransformer(TRANSFORMATIONS).transform(new File(args[0])) + RESET);
    }
}
