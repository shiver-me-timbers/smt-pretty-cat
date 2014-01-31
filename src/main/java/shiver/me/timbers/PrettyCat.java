package shiver.me.timbers;

import shiver.me.timbers.exceptions.ExceptionHandler;
import shiver.me.timbers.exceptions.FileNotFoundExceptionHandler;
import shiver.me.timbers.exceptions.IterableExceptionHandlers;
import shiver.me.timbers.exceptions.MissingFileNameArgumentExceptionHandler;
import shiver.me.timbers.exceptions.RethrowingExceptionHandler;
import shiver.me.timbers.java.LazyJavaWrappedTransformer;
import shiver.me.timbers.json.LazyJsonWrappedTransformer;
import shiver.me.timbers.transform.Container;
import shiver.me.timbers.transform.MultiFileTransformer;
import shiver.me.timbers.transform.Transformers;
import shiver.me.timbers.transform.antlr4.TokenTransformation;
import shiver.me.timbers.transform.composite.CompositeFileTransformer;
import shiver.me.timbers.transform.iterable.IterableTransformers;
import shiver.me.timbers.xml.LazyXmlWrappedTransformer;

import java.io.File;
import java.util.LinkedList;
import java.util.concurrent.Callable;

import static java.lang.System.exit;
import static java.lang.System.out;
import static shiver.me.timbers.BACKGROUND_COLOUR.BLACK;
import static shiver.me.timbers.ESCAPE.RESET;
import static shiver.me.timbers.FOREGROUND_COLOUR.BRIGHT_WHITE;
import static shiver.me.timbers.exceptions.Exceptions.withExceptionHandling;
import static shiver.me.timbers.transform.NullCompositeTokenFileTransformer.NULL_COMPOSITE_TOKEN_FILE_TRANSFORMER;

/**
 * This application can be used to print different types of source code to the terminal with syntactic highlighting.
 */
public class PrettyCat {

    private static final Transformers<CompositeFileTransformer<TokenTransformation>> TRANSFORMERS =
            new IterableTransformers<CompositeFileTransformer<TokenTransformation>>(
                    new LinkedList<CompositeFileTransformer<TokenTransformation>>() {{
                        add(new LazyJavaWrappedTransformer());
                        add(new LazyXmlWrappedTransformer());
                        add(new LazyJsonWrappedTransformer());
                    }},
                    NULL_COMPOSITE_TOKEN_FILE_TRANSFORMER
            );

    private static final Container<Class, ExceptionHandler> EXCEPTION_HANDLERS =
            new IterableExceptionHandlers(
                    new LinkedList<ExceptionHandler>() {{
                        add(new MissingFileNameArgumentExceptionHandler());
                        add(new FileNotFoundExceptionHandler());
                    }},
                    new RethrowingExceptionHandler<Throwable>(Throwable.class)
            );

    public static void main(final String[] args) throws Throwable {

        exit(run(args));
    }

    public static int run(final String[] args) throws Throwable {

        return withExceptionHandling(EXCEPTION_HANDLERS, new Callable<Void>() {

            @Override
            public Void call() throws Exception {

                for (String fileName : args) {

                    out.print(new MultiFileTransformer(TRANSFORMERS, BLACK, BRIGHT_WHITE).transform(new File(fileName)));
                    // Reset the colour scheme after printing the highlighted source code.
                    out.println(RESET);
                }

                return null;
            }
        });
    }
}
