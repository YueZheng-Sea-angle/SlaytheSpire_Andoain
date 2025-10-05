/*      */ package org.apache.logging.log4j.core.tools.picocli;
/*      */ 
/*      */ import java.io.File;
/*      */ import java.io.PrintStream;
/*      */ import java.lang.annotation.ElementType;
/*      */ import java.lang.annotation.Retention;
/*      */ import java.lang.annotation.RetentionPolicy;
/*      */ import java.lang.annotation.Target;
/*      */ import java.lang.reflect.Array;
/*      */ import java.lang.reflect.Constructor;
/*      */ import java.lang.reflect.Field;
/*      */ import java.lang.reflect.ParameterizedType;
/*      */ import java.lang.reflect.Type;
/*      */ import java.lang.reflect.WildcardType;
/*      */ import java.math.BigDecimal;
/*      */ import java.math.BigInteger;
/*      */ import java.net.InetAddress;
/*      */ import java.net.MalformedURLException;
/*      */ import java.net.URI;
/*      */ import java.net.URISyntaxException;
/*      */ import java.net.URL;
/*      */ import java.nio.charset.Charset;
/*      */ import java.nio.file.Path;
/*      */ import java.nio.file.Paths;
/*      */ import java.sql.Time;
/*      */ import java.text.BreakIterator;
/*      */ import java.text.ParseException;
/*      */ import java.text.SimpleDateFormat;
/*      */ import java.util.ArrayList;
/*      */ import java.util.Arrays;
/*      */ import java.util.Collection;
/*      */ import java.util.Collections;
/*      */ import java.util.Comparator;
/*      */ import java.util.Date;
/*      */ import java.util.HashMap;
/*      */ import java.util.HashSet;
/*      */ import java.util.LinkedHashMap;
/*      */ import java.util.LinkedHashSet;
/*      */ import java.util.LinkedList;
/*      */ import java.util.List;
/*      */ import java.util.Locale;
/*      */ import java.util.Map;
/*      */ import java.util.Queue;
/*      */ import java.util.Set;
/*      */ import java.util.SortedSet;
/*      */ import java.util.Stack;
/*      */ import java.util.TreeSet;
/*      */ import java.util.UUID;
/*      */ import java.util.concurrent.Callable;
/*      */ import java.util.regex.Pattern;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ public class CommandLine
/*      */ {
/*      */   public static final String VERSION = "2.0.3";
/*  137 */   private final Tracer tracer = new Tracer();
/*      */   private final Interpreter interpreter;
/*  139 */   private String commandName = "<main class>";
/*      */   private boolean overwrittenOptionsAllowed = false;
/*      */   private boolean unmatchedArgumentsAllowed = false;
/*  142 */   private final List<String> unmatchedArguments = new ArrayList<>();
/*      */   private CommandLine parent;
/*      */   private boolean usageHelpRequested;
/*      */   private boolean versionHelpRequested;
/*  146 */   private final List<String> versionLines = new ArrayList<>();
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CommandLine(Object command) {
/*  156 */     this.interpreter = new Interpreter(command);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CommandLine addSubcommand(String name, Object command) {
/*  201 */     CommandLine commandLine = toCommandLine(command);
/*  202 */     commandLine.parent = this;
/*  203 */     this.interpreter.commands.put(name, commandLine);
/*  204 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public Map<String, CommandLine> getSubcommands() {
/*  211 */     return new LinkedHashMap<>(this.interpreter.commands);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CommandLine getParent() {
/*  221 */     return this.parent;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <T> T getCommand() {
/*  230 */     return (T)this.interpreter.command;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUsageHelpRequested() {
/*  236 */     return this.usageHelpRequested;
/*      */   }
/*      */ 
/*      */   
/*      */   public boolean isVersionHelpRequested() {
/*  241 */     return this.versionHelpRequested;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isOverwrittenOptionsAllowed() {
/*  250 */     return this.overwrittenOptionsAllowed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CommandLine setOverwrittenOptionsAllowed(boolean newValue) {
/*  263 */     this.overwrittenOptionsAllowed = newValue;
/*  264 */     for (CommandLine command : this.interpreter.commands.values()) {
/*  265 */       command.setOverwrittenOptionsAllowed(newValue);
/*      */     }
/*  267 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public boolean isUnmatchedArgumentsAllowed() {
/*  278 */     return this.unmatchedArgumentsAllowed;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CommandLine setUnmatchedArgumentsAllowed(boolean newValue) {
/*  292 */     this.unmatchedArgumentsAllowed = newValue;
/*  293 */     for (CommandLine command : this.interpreter.commands.values()) {
/*  294 */       command.setUnmatchedArgumentsAllowed(newValue);
/*      */     }
/*  296 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<String> getUnmatchedArguments() {
/*  305 */     return this.unmatchedArguments;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <T> T populateCommand(T command, String... args) {
/*  329 */     CommandLine cli = toCommandLine(command);
/*  330 */     cli.parse(args);
/*  331 */     return command;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<CommandLine> parse(String... args) {
/*  349 */     return this.interpreter.parse(args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class DefaultExceptionHandler
/*      */     implements IExceptionHandler
/*      */   {
/*      */     public List<Object> handleException(CommandLine.ParameterException ex, PrintStream out, CommandLine.Help.Ansi ansi, String... args) {
/*  411 */       out.println(ex.getMessage());
/*  412 */       ex.getCommandLine().usage(out, ansi);
/*  413 */       return Collections.emptyList();
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static boolean printHelpIfRequested(List<CommandLine> parsedCommands, PrintStream out, Help.Ansi ansi) {
/*  433 */     for (CommandLine parsed : parsedCommands) {
/*  434 */       if (parsed.isUsageHelpRequested()) {
/*  435 */         parsed.usage(out, ansi);
/*  436 */         return true;
/*  437 */       }  if (parsed.isVersionHelpRequested()) {
/*  438 */         parsed.printVersionHelp(out, ansi);
/*  439 */         return true;
/*      */       } 
/*      */     } 
/*  442 */     return false;
/*      */   }
/*      */   private static Object execute(CommandLine parsed) {
/*  445 */     Object command = parsed.getCommand();
/*  446 */     if (command instanceof Runnable)
/*      */       try {
/*  448 */         ((Runnable)command).run();
/*  449 */         return null;
/*  450 */       } catch (Exception ex) {
/*  451 */         throw new ExecutionException(parsed, "Error while running command (" + command + ")", ex);
/*      */       }  
/*  453 */     if (command instanceof Callable) {
/*      */       try {
/*  455 */         return ((Callable)command).call();
/*  456 */       } catch (Exception ex) {
/*  457 */         throw new ExecutionException(parsed, "Error while calling command (" + command + ")", ex);
/*      */       } 
/*      */     }
/*  460 */     throw new ExecutionException(parsed, "Parsed command (" + command + ") is not Runnable or Callable");
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class RunFirst
/*      */     implements IParseResultHandler
/*      */   {
/*      */     public List<Object> handleParseResult(List<CommandLine> parsedCommands, PrintStream out, CommandLine.Help.Ansi ansi) {
/*  486 */       if (CommandLine.printHelpIfRequested(parsedCommands, out, ansi)) return Collections.emptyList(); 
/*  487 */       return Arrays.asList(new Object[] { CommandLine.access$300(parsedCommands.get(0)) });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class RunLast
/*      */     implements IParseResultHandler
/*      */   {
/*      */     public List<Object> handleParseResult(List<CommandLine> parsedCommands, PrintStream out, CommandLine.Help.Ansi ansi) {
/*  537 */       if (CommandLine.printHelpIfRequested(parsedCommands, out, ansi)) return Collections.emptyList(); 
/*  538 */       CommandLine last = parsedCommands.get(parsedCommands.size() - 1);
/*  539 */       return Arrays.asList(new Object[] { CommandLine.access$300(last) });
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class RunAll
/*      */     implements IParseResultHandler
/*      */   {
/*      */     public List<Object> handleParseResult(List<CommandLine> parsedCommands, PrintStream out, CommandLine.Help.Ansi ansi) {
/*  563 */       if (CommandLine.printHelpIfRequested(parsedCommands, out, ansi)) {
/*  564 */         return null;
/*      */       }
/*  566 */       List<Object> result = new ArrayList();
/*  567 */       for (CommandLine parsed : parsedCommands) {
/*  568 */         result.add(CommandLine.execute(parsed));
/*      */       }
/*  570 */       return result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Object> parseWithHandler(IParseResultHandler handler, PrintStream out, String... args) {
/*  611 */     return parseWithHandlers(handler, out, Help.Ansi.AUTO, new DefaultExceptionHandler(), args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public List<Object> parseWithHandlers(IParseResultHandler handler, PrintStream out, Help.Ansi ansi, IExceptionHandler exceptionHandler, String... args) {
/*      */     try {
/*  657 */       List<CommandLine> result = parse(args);
/*  658 */       return handler.handleParseResult(result, out, ansi);
/*  659 */     } catch (ParameterException ex) {
/*  660 */       return exceptionHandler.handleException(ex, out, ansi, args);
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void usage(Object command, PrintStream out) {
/*  670 */     toCommandLine(command).usage(out);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void usage(Object command, PrintStream out, Help.Ansi ansi) {
/*  682 */     toCommandLine(command).usage(out, ansi);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static void usage(Object command, PrintStream out, Help.ColorScheme colorScheme) {
/*  694 */     toCommandLine(command).usage(out, colorScheme);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void usage(PrintStream out) {
/*  703 */     usage(out, Help.Ansi.AUTO);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void usage(PrintStream out, Help.Ansi ansi) {
/*  713 */     usage(out, Help.defaultColorScheme(ansi));
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void usage(PrintStream out, Help.ColorScheme colorScheme) {
/*  748 */     Help help = (new Help(this.interpreter.command, colorScheme)).addAllSubcommands(getSubcommands());
/*  749 */     if (!"=".equals(getSeparator())) {
/*  750 */       help.separator = getSeparator();
/*  751 */       help.parameterLabelRenderer = help.createDefaultParamLabelRenderer();
/*      */     } 
/*  753 */     if (!"<main class>".equals(getCommandName())) {
/*  754 */       help.commandName = getCommandName();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*  770 */     StringBuilder sb = (new StringBuilder()).append(help.headerHeading(new Object[0])).append(help.header(new Object[0])).append(help.synopsisHeading(new Object[0])).append(help.synopsis(help.synopsisHeadingLength())).append(help.descriptionHeading(new Object[0])).append(help.description(new Object[0])).append(help.parameterListHeading(new Object[0])).append(help.parameterList()).append(help.optionListHeading(new Object[0])).append(help.optionList()).append(help.commandListHeading(new Object[0])).append(help.commandList()).append(help.footerHeading(new Object[0])).append(help.footer(new Object[0]));
/*  771 */     out.print(sb);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void printVersionHelp(PrintStream out) {
/*  780 */     printVersionHelp(out, Help.Ansi.AUTO);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void printVersionHelp(PrintStream out, Help.Ansi ansi) {
/*  794 */     for (String versionInfo : this.versionLines) {
/*  795 */       ansi.getClass(); out.println(new Help.Ansi.Text(versionInfo));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public void printVersionHelp(PrintStream out, Help.Ansi ansi, Object... params) {
/*  812 */     for (String versionInfo : this.versionLines) {
/*  813 */       ansi.getClass(); out.println(new Help.Ansi.Text(String.format(versionInfo, params)));
/*      */     } 
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <C extends Callable<T>, T> T call(C callable, PrintStream out, String... args) {
/*  836 */     return call(callable, out, Help.Ansi.AUTO, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <C extends Callable<T>, T> T call(C callable, PrintStream out, Help.Ansi ansi, String... args) {
/*  884 */     CommandLine cmd = new CommandLine(callable);
/*  885 */     List<Object> results = cmd.parseWithHandlers(new RunLast(), out, ansi, new DefaultExceptionHandler(), args);
/*  886 */     return (results == null || results.isEmpty()) ? null : (T)results.get(0);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <R extends Runnable> void run(R runnable, PrintStream out, String... args) {
/*  906 */     run(runnable, out, Help.Ansi.AUTO, args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static <R extends Runnable> void run(R runnable, PrintStream out, Help.Ansi ansi, String... args) {
/*  952 */     CommandLine cmd = new CommandLine(runnable);
/*  953 */     cmd.parseWithHandlers(new RunLast(), out, ansi, new DefaultExceptionHandler(), args);
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public <K> CommandLine registerConverter(Class<K> cls, ITypeConverter<K> converter) {
/* 1000 */     this.interpreter.converterRegistry.put(Assert.notNull(cls, "class"), Assert.notNull(converter, "converter"));
/* 1001 */     for (CommandLine command : this.interpreter.commands.values()) {
/* 1002 */       command.registerConverter(cls, converter);
/*      */     }
/* 1004 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getSeparator() {
/* 1010 */     return this.interpreter.separator;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CommandLine setSeparator(String separator) {
/* 1018 */     this.interpreter.separator = Assert.<String>notNull(separator, "separator");
/* 1019 */     return this;
/*      */   }
/*      */ 
/*      */ 
/*      */   
/*      */   public String getCommandName() {
/* 1025 */     return this.commandName;
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public CommandLine setCommandName(String commandName) {
/* 1034 */     this.commandName = Assert.<String>notNull(commandName, "commandName");
/* 1035 */     return this;
/*      */   }
/* 1037 */   private static boolean empty(String str) { return (str == null || str.trim().length() == 0); }
/* 1038 */   private static boolean empty(Object[] array) { return (array == null || array.length == 0); }
/* 1039 */   private static boolean empty(Help.Ansi.Text txt) { return (txt == null || txt.plain.toString().trim().length() == 0); }
/* 1040 */   private static String str(String[] arr, int i) { return (arr == null || arr.length == 0) ? "" : arr[i]; }
/* 1041 */   private static boolean isBoolean(Class<?> type) { return (type == Boolean.class || type == boolean.class); }
/* 1042 */   private static CommandLine toCommandLine(Object obj) { return (obj instanceof CommandLine) ? (CommandLine)obj : new CommandLine(obj); }
/* 1043 */   private static boolean isMultiValue(Field field) { return isMultiValue(field.getType()); } private static boolean isMultiValue(Class<?> cls) {
/* 1044 */     return (cls.isArray() || Collection.class.isAssignableFrom(cls) || Map.class.isAssignableFrom(cls));
/*      */   } private static Class<?>[] getTypeAttribute(Field field) {
/* 1046 */     Class<?>[] explicit = field.isAnnotationPresent((Class)Parameters.class) ? ((Parameters)field.<Parameters>getAnnotation(Parameters.class)).type() : ((Option)field.<Option>getAnnotation(Option.class)).type();
/* 1047 */     if (explicit.length > 0) return explicit; 
/* 1048 */     if (field.getType().isArray()) return new Class[] { field.getType().getComponentType() }; 
/* 1049 */     if (isMultiValue(field)) {
/* 1050 */       Type type = field.getGenericType();
/* 1051 */       if (type instanceof ParameterizedType) {
/* 1052 */         ParameterizedType parameterizedType = (ParameterizedType)type;
/* 1053 */         Type[] paramTypes = parameterizedType.getActualTypeArguments();
/* 1054 */         Class<?>[] result = new Class[paramTypes.length];
/* 1055 */         for (int i = 0; i < paramTypes.length; i++) {
/* 1056 */           if (paramTypes[i] instanceof Class) { result[i] = (Class)paramTypes[i]; }
/* 1057 */           else if (paramTypes[i] instanceof WildcardType)
/* 1058 */           { WildcardType wildcardType = (WildcardType)paramTypes[i];
/* 1059 */             Type[] lower = wildcardType.getLowerBounds();
/* 1060 */             if (lower.length > 0 && lower[0] instanceof Class) { result[i] = (Class)lower[0]; }
/* 1061 */             else { Type[] upper = wildcardType.getUpperBounds();
/* 1062 */               if (upper.length > 0 && upper[0] instanceof Class) { result[i] = (Class)upper[0]; }
/*      */               else
/* 1064 */               { Arrays.fill((Object[])result, String.class); return result; }  }  } else { Arrays.fill((Object[])result, String.class); return result; }
/*      */         
/* 1066 */         }  return result;
/*      */       } 
/* 1068 */       return new Class[] { String.class, String.class };
/*      */     } 
/* 1070 */     return new Class[] { field.getType() };
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Range
/*      */     implements Comparable<Range>
/*      */   {
/*      */     public final int min;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final int max;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public final boolean isVariable;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final boolean isUnspecified;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final String originalValue;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Range(int min, int max, boolean variable, boolean unspecified, String originalValue) {
/* 1660 */       this.min = min;
/* 1661 */       this.max = max;
/* 1662 */       this.isVariable = variable;
/* 1663 */       this.isUnspecified = unspecified;
/* 1664 */       this.originalValue = originalValue;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Range optionArity(Field field) {
/* 1671 */       return field.isAnnotationPresent((Class)CommandLine.Option.class) ? 
/* 1672 */         adjustForType(valueOf(((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).arity()), field) : new Range(0, 0, false, true, "0");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Range parameterArity(Field field) {
/* 1680 */       return field.isAnnotationPresent((Class)CommandLine.Parameters.class) ? 
/* 1681 */         adjustForType(valueOf(((CommandLine.Parameters)field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).arity()), field) : new Range(0, 0, false, true, "0");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Range parameterIndex(Field field) {
/* 1688 */       return field.isAnnotationPresent((Class)CommandLine.Parameters.class) ? 
/* 1689 */         valueOf(((CommandLine.Parameters)field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).index()) : new Range(0, 0, false, true, "0");
/*      */     }
/*      */     
/*      */     static Range adjustForType(Range result, Field field) {
/* 1693 */       return result.isUnspecified ? defaultArity(field) : result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Range defaultArity(Field field) {
/* 1702 */       Class<?> type = field.getType();
/* 1703 */       if (field.isAnnotationPresent((Class)CommandLine.Option.class)) {
/* 1704 */         return defaultArity(type);
/*      */       }
/* 1706 */       if (CommandLine.isMultiValue(type)) {
/* 1707 */         return valueOf("0..1");
/*      */       }
/* 1709 */       return valueOf("1");
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public static Range defaultArity(Class<?> type) {
/* 1715 */       return CommandLine.isBoolean(type) ? valueOf("0") : valueOf("1");
/*      */     } private int size() {
/* 1717 */       return 1 + this.max - this.min;
/*      */     } static Range parameterCapacity(Field field) {
/* 1719 */       Range arity = parameterArity(field);
/* 1720 */       if (!CommandLine.isMultiValue(field)) return arity; 
/* 1721 */       Range index = parameterIndex(field);
/* 1722 */       if (arity.max == 0) return arity; 
/* 1723 */       if (index.size() == 1) return arity; 
/* 1724 */       if (index.isVariable) return valueOf(arity.min + "..*"); 
/* 1725 */       if (arity.size() == 1) return valueOf((arity.min * index.size()) + ""); 
/* 1726 */       if (arity.isVariable) return valueOf((arity.min * index.size()) + "..*"); 
/* 1727 */       return valueOf((arity.min * index.size()) + ".." + (arity.max * index.size()));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static Range valueOf(String range) {
/* 1736 */       range = range.trim();
/* 1737 */       boolean unspecified = (range.length() == 0 || range.startsWith(".."));
/* 1738 */       int min = -1, max = -1;
/* 1739 */       boolean variable = false;
/* 1740 */       int dots = -1;
/* 1741 */       if ((dots = range.indexOf("..")) >= 0) {
/* 1742 */         min = parseInt(range.substring(0, dots), 0);
/* 1743 */         max = parseInt(range.substring(dots + 2), 2147483647);
/* 1744 */         variable = (max == Integer.MAX_VALUE);
/*      */       } else {
/* 1746 */         max = parseInt(range, 2147483647);
/* 1747 */         variable = (max == Integer.MAX_VALUE);
/* 1748 */         min = variable ? 0 : max;
/*      */       } 
/* 1750 */       Range result = new Range(min, max, variable, unspecified, range);
/* 1751 */       return result;
/*      */     }
/*      */     private static int parseInt(String str, int defaultValue) {
/*      */       try {
/* 1755 */         return Integer.parseInt(str);
/* 1756 */       } catch (Exception ex) {
/* 1757 */         return defaultValue;
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Range min(int newMin) {
/* 1764 */       return new Range(newMin, Math.max(newMin, this.max), this.isVariable, this.isUnspecified, this.originalValue);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Range max(int newMax) {
/* 1770 */       return new Range(Math.min(this.min, newMax), newMax, this.isVariable, this.isUnspecified, this.originalValue);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public boolean contains(int value) {
/* 1777 */       return (this.min <= value && this.max >= value);
/*      */     }
/*      */     
/*      */     public boolean equals(Object object) {
/* 1781 */       if (!(object instanceof Range)) return false; 
/* 1782 */       Range other = (Range)object;
/* 1783 */       return (other.max == this.max && other.min == this.min && other.isVariable == this.isVariable);
/*      */     }
/*      */     
/*      */     public int hashCode() {
/* 1787 */       return ((629 + this.max) * 37 + this.min) * 37 + (this.isVariable ? 1 : 0);
/*      */     }
/*      */     
/*      */     public String toString() {
/* 1791 */       return (this.min == this.max) ? String.valueOf(this.min) : (this.min + ".." + (this.isVariable ? "*" : (String)Integer.valueOf(this.max)));
/*      */     }
/*      */     
/*      */     public int compareTo(Range other) {
/* 1795 */       int result = this.min - other.min;
/* 1796 */       return (result == 0) ? (this.max - other.max) : result;
/*      */     }
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   static void init(Class<?> cls, List<Field> requiredFields, Map<String, Field> optionName2Field, Map<Character, Field> singleCharOption2Field, List<Field> positionalParametersFields) {
/* 1804 */     Field[] declaredFields = cls.getDeclaredFields();
/* 1805 */     for (Field field : declaredFields) {
/* 1806 */       field.setAccessible(true);
/* 1807 */       if (field.isAnnotationPresent((Class)Option.class)) {
/* 1808 */         Option option = field.<Option>getAnnotation(Option.class);
/* 1809 */         if (option.required()) {
/* 1810 */           requiredFields.add(field);
/*      */         }
/* 1812 */         for (String name : option.names()) {
/* 1813 */           Field existing = optionName2Field.put(name, field);
/* 1814 */           if (existing != null && existing != field) {
/* 1815 */             throw DuplicateOptionAnnotationsException.create(name, field, existing);
/*      */           }
/* 1817 */           if (name.length() == 2 && name.startsWith("-")) {
/* 1818 */             char flag = name.charAt(1);
/* 1819 */             Field existing2 = singleCharOption2Field.put(Character.valueOf(flag), field);
/* 1820 */             if (existing2 != null && existing2 != field) {
/* 1821 */               throw DuplicateOptionAnnotationsException.create(name, field, existing2);
/*      */             }
/*      */           } 
/*      */         } 
/*      */       } 
/* 1826 */       if (field.isAnnotationPresent((Class)Parameters.class)) {
/* 1827 */         if (field.isAnnotationPresent((Class)Option.class)) {
/* 1828 */           throw new DuplicateOptionAnnotationsException("A field can be either @Option or @Parameters, but '" + field
/* 1829 */               .getName() + "' is both.");
/*      */         }
/* 1831 */         positionalParametersFields.add(field);
/* 1832 */         Range arity = Range.parameterArity(field);
/* 1833 */         if (arity.min > 0)
/* 1834 */           requiredFields.add(field); 
/*      */       } 
/*      */     } 
/*      */   }
/*      */   
/*      */   static void validatePositionalParameters(List<Field> positionalParametersFields) {
/* 1840 */     int min = 0;
/* 1841 */     for (Field field : positionalParametersFields) {
/* 1842 */       Range index = Range.parameterIndex(field);
/* 1843 */       if (index.min > min) {
/* 1844 */         throw new ParameterIndexGapException("Missing field annotated with @Parameter(index=" + min + "). Nearest field '" + field
/* 1845 */             .getName() + "' has index=" + index.min);
/*      */       }
/* 1847 */       min = Math.max(min, index.max);
/* 1848 */       min = (min == Integer.MAX_VALUE) ? min : (min + 1);
/*      */     } 
/*      */   }
/*      */   private static <T> Stack<T> reverse(Stack<T> stack) {
/* 1852 */     Collections.reverse(stack);
/* 1853 */     return stack;
/*      */   }
/*      */ 
/*      */   
/*      */   private class Interpreter
/*      */   {
/* 1859 */     private final Map<String, CommandLine> commands = new LinkedHashMap<>();
/* 1860 */     private final Map<Class<?>, CommandLine.ITypeConverter<?>> converterRegistry = new HashMap<>();
/* 1861 */     private final Map<String, Field> optionName2Field = new HashMap<>();
/* 1862 */     private final Map<Character, Field> singleCharOption2Field = new HashMap<>();
/* 1863 */     private final List<Field> requiredFields = new ArrayList<>();
/* 1864 */     private final List<Field> positionalParametersFields = new ArrayList<>();
/*      */     private final Object command;
/*      */     private boolean isHelpRequested;
/* 1867 */     private String separator = "=";
/*      */     private int position;
/*      */     
/*      */     Interpreter(Object command) {
/* 1871 */       this.converterRegistry.put(Path.class, new CommandLine.BuiltIn.PathConverter());
/* 1872 */       this.converterRegistry.put(Object.class, new CommandLine.BuiltIn.StringConverter());
/* 1873 */       this.converterRegistry.put(String.class, new CommandLine.BuiltIn.StringConverter());
/* 1874 */       this.converterRegistry.put(StringBuilder.class, new CommandLine.BuiltIn.StringBuilderConverter());
/* 1875 */       this.converterRegistry.put(CharSequence.class, new CommandLine.BuiltIn.CharSequenceConverter());
/* 1876 */       this.converterRegistry.put(Byte.class, new CommandLine.BuiltIn.ByteConverter());
/* 1877 */       this.converterRegistry.put(byte.class, new CommandLine.BuiltIn.ByteConverter());
/* 1878 */       this.converterRegistry.put(Boolean.class, new CommandLine.BuiltIn.BooleanConverter());
/* 1879 */       this.converterRegistry.put(boolean.class, new CommandLine.BuiltIn.BooleanConverter());
/* 1880 */       this.converterRegistry.put(Character.class, new CommandLine.BuiltIn.CharacterConverter());
/* 1881 */       this.converterRegistry.put(char.class, new CommandLine.BuiltIn.CharacterConverter());
/* 1882 */       this.converterRegistry.put(Short.class, new CommandLine.BuiltIn.ShortConverter());
/* 1883 */       this.converterRegistry.put(short.class, new CommandLine.BuiltIn.ShortConverter());
/* 1884 */       this.converterRegistry.put(Integer.class, new CommandLine.BuiltIn.IntegerConverter());
/* 1885 */       this.converterRegistry.put(int.class, new CommandLine.BuiltIn.IntegerConverter());
/* 1886 */       this.converterRegistry.put(Long.class, new CommandLine.BuiltIn.LongConverter());
/* 1887 */       this.converterRegistry.put(long.class, new CommandLine.BuiltIn.LongConverter());
/* 1888 */       this.converterRegistry.put(Float.class, new CommandLine.BuiltIn.FloatConverter());
/* 1889 */       this.converterRegistry.put(float.class, new CommandLine.BuiltIn.FloatConverter());
/* 1890 */       this.converterRegistry.put(Double.class, new CommandLine.BuiltIn.DoubleConverter());
/* 1891 */       this.converterRegistry.put(double.class, new CommandLine.BuiltIn.DoubleConverter());
/* 1892 */       this.converterRegistry.put(File.class, new CommandLine.BuiltIn.FileConverter());
/* 1893 */       this.converterRegistry.put(URI.class, new CommandLine.BuiltIn.URIConverter());
/* 1894 */       this.converterRegistry.put(URL.class, new CommandLine.BuiltIn.URLConverter());
/* 1895 */       this.converterRegistry.put(Date.class, new CommandLine.BuiltIn.ISO8601DateConverter());
/* 1896 */       this.converterRegistry.put(Time.class, new CommandLine.BuiltIn.ISO8601TimeConverter());
/* 1897 */       this.converterRegistry.put(BigDecimal.class, new CommandLine.BuiltIn.BigDecimalConverter());
/* 1898 */       this.converterRegistry.put(BigInteger.class, new CommandLine.BuiltIn.BigIntegerConverter());
/* 1899 */       this.converterRegistry.put(Charset.class, new CommandLine.BuiltIn.CharsetConverter());
/* 1900 */       this.converterRegistry.put(InetAddress.class, new CommandLine.BuiltIn.InetAddressConverter());
/* 1901 */       this.converterRegistry.put(Pattern.class, new CommandLine.BuiltIn.PatternConverter());
/* 1902 */       this.converterRegistry.put(UUID.class, new CommandLine.BuiltIn.UUIDConverter());
/*      */       
/* 1904 */       this.command = CommandLine.Assert.notNull(command, "command");
/* 1905 */       Class<?> cls = command.getClass();
/* 1906 */       String declaredName = null;
/* 1907 */       String declaredSeparator = null;
/* 1908 */       boolean hasCommandAnnotation = false;
/* 1909 */       while (cls != null) {
/* 1910 */         CommandLine.init(cls, this.requiredFields, this.optionName2Field, this.singleCharOption2Field, this.positionalParametersFields);
/* 1911 */         if (cls.isAnnotationPresent((Class)CommandLine.Command.class)) {
/* 1912 */           hasCommandAnnotation = true;
/* 1913 */           CommandLine.Command cmd = cls.<CommandLine.Command>getAnnotation(CommandLine.Command.class);
/* 1914 */           declaredSeparator = (declaredSeparator == null) ? cmd.separator() : declaredSeparator;
/* 1915 */           declaredName = (declaredName == null) ? cmd.name() : declaredName;
/* 1916 */           CommandLine.this.versionLines.addAll(Arrays.asList(cmd.version()));
/*      */           
/* 1918 */           for (Class<?> sub : cmd.subcommands()) {
/* 1919 */             CommandLine.Command subCommand = sub.<CommandLine.Command>getAnnotation(CommandLine.Command.class);
/* 1920 */             if (subCommand == null || "<main class>".equals(subCommand.name())) {
/* 1921 */               throw new CommandLine.InitializationException("Subcommand " + sub.getName() + " is missing the mandatory @Command annotation with a 'name' attribute");
/*      */             }
/*      */ 
/*      */             
/* 1925 */             try { Constructor<?> constructor = sub.getDeclaredConstructor(new Class[0]);
/* 1926 */               constructor.setAccessible(true);
/* 1927 */               CommandLine commandLine = CommandLine.toCommandLine(constructor.newInstance(new Object[0]));
/* 1928 */               commandLine.parent = CommandLine.this;
/* 1929 */               this.commands.put(subCommand.name(), commandLine); }
/*      */             catch (InitializationException ex)
/* 1931 */             { throw ex; }
/* 1932 */             catch (NoSuchMethodException ex) { throw new CommandLine.InitializationException("Cannot instantiate subcommand " + sub
/* 1933 */                   .getName() + ": the class has no constructor", ex); }
/* 1934 */             catch (Exception ex)
/* 1935 */             { throw new CommandLine.InitializationException("Could not instantiate and add subcommand " + sub
/* 1936 */                   .getName() + ": " + ex, ex); }
/*      */           
/*      */           } 
/*      */         } 
/* 1940 */         cls = cls.getSuperclass();
/*      */       } 
/* 1942 */       this.separator = (declaredSeparator != null) ? declaredSeparator : this.separator;
/* 1943 */       CommandLine.this.commandName = (declaredName != null) ? declaredName : CommandLine.this.commandName;
/* 1944 */       Collections.sort(this.positionalParametersFields, new CommandLine.PositionalParametersSorter());
/* 1945 */       CommandLine.validatePositionalParameters(this.positionalParametersFields);
/*      */       
/* 1947 */       if (this.positionalParametersFields.isEmpty() && this.optionName2Field.isEmpty() && !hasCommandAnnotation) {
/* 1948 */         throw new CommandLine.InitializationException(command + " (" + command.getClass() + ") is not a command: it has no @Command, @Option or @Parameters annotations");
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     List<CommandLine> parse(String... args) {
/* 1960 */       CommandLine.Assert.notNull(args, "argument array");
/* 1961 */       if (CommandLine.this.tracer.isInfo()) CommandLine.this.tracer.info("Parsing %d command line args %s%n", new Object[] { Integer.valueOf(args.length), Arrays.toString((Object[])args) }); 
/* 1962 */       Stack<String> arguments = new Stack<>();
/* 1963 */       for (int i = args.length - 1; i >= 0; i--) {
/* 1964 */         arguments.push(args[i]);
/*      */       }
/* 1966 */       List<CommandLine> result = new ArrayList<>();
/* 1967 */       parse(result, arguments, args);
/* 1968 */       return result;
/*      */     }
/*      */ 
/*      */     
/*      */     private void parse(List<CommandLine> parsedCommands, Stack<String> argumentStack, String[] originalArgs) {
/* 1973 */       this.isHelpRequested = false;
/* 1974 */       CommandLine.this.versionHelpRequested = false;
/* 1975 */       CommandLine.this.usageHelpRequested = false;
/*      */       
/* 1977 */       Class<?> cmdClass = this.command.getClass();
/* 1978 */       if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Initializing %s: %d options, %d positional parameters, %d required, %d subcommands.%n", new Object[] { cmdClass.getName(), Integer.valueOf((new HashSet(this.optionName2Field.values())).size()), Integer.valueOf(this.positionalParametersFields.size()), Integer.valueOf(this.requiredFields.size()), Integer.valueOf(this.commands.size()) }); 
/* 1979 */       parsedCommands.add(CommandLine.this);
/* 1980 */       List<Field> required = new ArrayList<>(this.requiredFields);
/* 1981 */       Set<Field> initialized = new HashSet<>();
/* 1982 */       Collections.sort(required, new CommandLine.PositionalParametersSorter());
/*      */       try {
/* 1984 */         processArguments(parsedCommands, argumentStack, required, initialized, originalArgs);
/* 1985 */       } catch (ParameterException ex) {
/* 1986 */         throw ex;
/* 1987 */       } catch (Exception ex) {
/* 1988 */         int offendingArgIndex = originalArgs.length - argumentStack.size() - 1;
/* 1989 */         String arg = (offendingArgIndex >= 0 && offendingArgIndex < originalArgs.length) ? originalArgs[offendingArgIndex] : "?";
/* 1990 */         throw CommandLine.ParameterException.create(CommandLine.this, ex, arg, offendingArgIndex, originalArgs);
/*      */       } 
/* 1992 */       if (!isAnyHelpRequested() && !required.isEmpty()) {
/* 1993 */         for (Field missing : required) {
/* 1994 */           if (missing.isAnnotationPresent((Class)CommandLine.Option.class)) {
/* 1995 */             throw CommandLine.MissingParameterException.create(CommandLine.this, required, this.separator);
/*      */           }
/* 1997 */           assertNoMissingParameters(missing, (CommandLine.Range.parameterArity(missing)).min, argumentStack);
/*      */         } 
/*      */       }
/*      */       
/* 2001 */       if (!CommandLine.this.unmatchedArguments.isEmpty()) {
/* 2002 */         if (!CommandLine.this.isUnmatchedArgumentsAllowed()) throw new CommandLine.UnmatchedArgumentException(CommandLine.this, CommandLine.this.unmatchedArguments); 
/* 2003 */         if (CommandLine.this.tracer.isWarn()) CommandLine.this.tracer.warn("Unmatched arguments: %s%n", new Object[] { CommandLine.access$2100(this.this$0) });
/*      */       
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void processArguments(List<CommandLine> parsedCommands, Stack<String> args, Collection<Field> required, Set<Field> initialized, String[] originalArgs) throws Exception {
/* 2021 */       while (!args.isEmpty()) {
/* 2022 */         String arg = args.pop();
/* 2023 */         if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Processing argument '%s'. Remainder=%s%n", new Object[] { arg, CommandLine.access$2200((Stack)args.clone()) });
/*      */ 
/*      */ 
/*      */         
/* 2027 */         if ("--".equals(arg)) {
/* 2028 */           CommandLine.this.tracer.info("Found end-of-options delimiter '--'. Treating remainder as positional parameters.%n", new Object[0]);
/* 2029 */           processRemainderAsPositionalParameters(required, initialized, args);
/*      */           
/*      */           return;
/*      */         } 
/*      */         
/* 2034 */         if (this.commands.containsKey(arg)) {
/* 2035 */           if (!this.isHelpRequested && !required.isEmpty()) {
/* 2036 */             throw CommandLine.MissingParameterException.create(CommandLine.this, required, this.separator);
/*      */           }
/* 2038 */           if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Found subcommand '%s' (%s)%n", new Object[] { arg, (CommandLine.access$2300((CommandLine)this.commands.get(arg))).command.getClass().getName() }); 
/* 2039 */           (this.commands.get(arg)).interpreter.parse(parsedCommands, args, originalArgs);
/*      */ 
/*      */ 
/*      */           
/*      */           return;
/*      */         } 
/*      */ 
/*      */         
/* 2047 */         boolean paramAttachedToOption = false;
/* 2048 */         int separatorIndex = arg.indexOf(this.separator);
/* 2049 */         if (separatorIndex > 0)
/* 2050 */         { String key = arg.substring(0, separatorIndex);
/*      */           
/* 2052 */           if (this.optionName2Field.containsKey(key) && !this.optionName2Field.containsKey(arg))
/* 2053 */           { paramAttachedToOption = true;
/* 2054 */             String optionParam = arg.substring(separatorIndex + this.separator.length());
/* 2055 */             args.push(optionParam);
/* 2056 */             arg = key;
/* 2057 */             if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Separated '%s' option from '%s' option parameter%n", new Object[] { key, optionParam });  }
/* 2058 */           else if (CommandLine.this.tracer.isDebug()) { CommandLine.this.tracer.debug("'%s' contains separator '%s' but '%s' is not a known option%n", new Object[] { arg, this.separator, key }); }  }
/* 2059 */         else if (CommandLine.this.tracer.isDebug()) { CommandLine.this.tracer.debug("'%s' cannot be separated into <option>%s<option-parameter>%n", new Object[] { arg, this.separator }); }
/* 2060 */          if (this.optionName2Field.containsKey(arg)) {
/* 2061 */           processStandaloneOption(required, initialized, arg, args, paramAttachedToOption);
/*      */           
/*      */           continue;
/*      */         } 
/* 2065 */         if (arg.length() > 2 && arg.startsWith("-")) {
/* 2066 */           if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Trying to process '%s' as clustered short options%n", new Object[] { arg, args }); 
/* 2067 */           processClusteredShortOptions(required, initialized, arg, args);
/*      */           
/*      */           continue;
/*      */         } 
/*      */         
/* 2072 */         args.push(arg);
/* 2073 */         if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Could not find option '%s', deciding whether to treat as unmatched option or positional parameter...%n", new Object[] { arg }); 
/* 2074 */         if (resemblesOption(arg)) { handleUnmatchedArguments(args.pop()); continue; }
/* 2075 */          if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("No option named '%s' found. Processing remainder as positional parameters%n", new Object[] { arg }); 
/* 2076 */         processPositionalParameter(required, initialized, args);
/*      */       } 
/*      */     }
/*      */     
/*      */     private boolean resemblesOption(String arg) {
/* 2081 */       int count = 0;
/* 2082 */       for (String optionName : this.optionName2Field.keySet()) {
/* 2083 */         for (int i = 0; i < arg.length() && 
/* 2084 */           optionName.length() > i && arg.charAt(i) == optionName.charAt(i); i++) count++;
/*      */       
/*      */       } 
/* 2087 */       boolean result = (count > 0 && count * 10 >= this.optionName2Field.size() * 9);
/* 2088 */       if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("%s %s an option: %d matching prefix chars out of %d option names%n", new Object[] { arg, result ? "resembles" : "doesn't resemble", Integer.valueOf(count), Integer.valueOf(this.optionName2Field.size()) }); 
/* 2089 */       return result;
/*      */     } private void handleUnmatchedArguments(String arg) {
/* 2091 */       Stack<String> args = new Stack<>(); args.add(arg); handleUnmatchedArguments(args);
/*      */     } private void handleUnmatchedArguments(Stack<String> args) {
/* 2093 */       for (; !args.isEmpty(); CommandLine.this.unmatchedArguments.add(args.pop()));
/*      */     }
/*      */     
/*      */     private void processRemainderAsPositionalParameters(Collection<Field> required, Set<Field> initialized, Stack<String> args) throws Exception {
/* 2097 */       while (!args.empty())
/* 2098 */         processPositionalParameter(required, initialized, args); 
/*      */     }
/*      */     
/*      */     private void processPositionalParameter(Collection<Field> required, Set<Field> initialized, Stack<String> args) throws Exception {
/* 2102 */       if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Processing next arg as a positional parameter at index=%d. Remainder=%s%n", new Object[] { Integer.valueOf(this.position), CommandLine.access$2200((Stack)args.clone()) }); 
/* 2103 */       int consumed = 0;
/* 2104 */       for (Field positionalParam : this.positionalParametersFields) {
/* 2105 */         CommandLine.Range indexRange = CommandLine.Range.parameterIndex(positionalParam);
/* 2106 */         if (!indexRange.contains(this.position)) {
/*      */           continue;
/*      */         }
/*      */ 
/*      */         
/* 2111 */         Stack<String> argsCopy = (Stack<String>)args.clone();
/* 2112 */         CommandLine.Range arity = CommandLine.Range.parameterArity(positionalParam);
/* 2113 */         if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Position %d is in index range %s. Trying to assign args to %s, arity=%s%n", new Object[] { Integer.valueOf(this.position), indexRange, positionalParam, arity }); 
/* 2114 */         assertNoMissingParameters(positionalParam, arity.min, argsCopy);
/* 2115 */         int originalSize = argsCopy.size();
/* 2116 */         applyOption(positionalParam, CommandLine.Parameters.class, arity, false, argsCopy, initialized, "args[" + indexRange + "] at position " + this.position);
/* 2117 */         int count = originalSize - argsCopy.size();
/* 2118 */         if (count > 0) required.remove(positionalParam); 
/* 2119 */         consumed = Math.max(consumed, count);
/*      */       } 
/*      */       
/* 2122 */       for (int i = 0; i < consumed; ) { args.pop(); i++; }
/* 2123 */        this.position += consumed;
/* 2124 */       if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Consumed %d arguments, moving position to index %d.%n", new Object[] { Integer.valueOf(consumed), Integer.valueOf(this.position) }); 
/* 2125 */       if (consumed == 0 && !args.isEmpty()) {
/* 2126 */         handleUnmatchedArguments(args.pop());
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void processStandaloneOption(Collection<Field> required, Set<Field> initialized, String arg, Stack<String> args, boolean paramAttachedToKey) throws Exception {
/* 2135 */       Field field = this.optionName2Field.get(arg);
/* 2136 */       required.remove(field);
/* 2137 */       CommandLine.Range arity = CommandLine.Range.optionArity(field);
/* 2138 */       if (paramAttachedToKey) {
/* 2139 */         arity = arity.min(Math.max(1, arity.min));
/*      */       }
/* 2141 */       if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Found option named '%s': field %s, arity=%s%n", new Object[] { arg, field, arity }); 
/* 2142 */       applyOption(field, CommandLine.Option.class, arity, paramAttachedToKey, args, initialized, "option " + arg);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void processClusteredShortOptions(Collection<Field> required, Set<Field> initialized, String arg, Stack<String> args) throws Exception {
/* 2150 */       String prefix = arg.substring(0, 1);
/* 2151 */       String cluster = arg.substring(1);
/* 2152 */       boolean paramAttachedToOption = true;
/*      */       
/* 2154 */       while (cluster.length() > 0 && this.singleCharOption2Field.containsKey(Character.valueOf(cluster.charAt(0)))) {
/* 2155 */         Field field = this.singleCharOption2Field.get(Character.valueOf(cluster.charAt(0)));
/* 2156 */         CommandLine.Range arity = CommandLine.Range.optionArity(field);
/* 2157 */         String argDescription = "option " + prefix + cluster.charAt(0);
/* 2158 */         if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Found option '%s%s' in %s: field %s, arity=%s%n", new Object[] { prefix, Character.valueOf(cluster.charAt(0)), arg, field, arity }); 
/* 2159 */         required.remove(field);
/* 2160 */         cluster = (cluster.length() > 0) ? cluster.substring(1) : "";
/* 2161 */         paramAttachedToOption = (cluster.length() > 0);
/* 2162 */         if (cluster.startsWith(this.separator)) {
/* 2163 */           cluster = cluster.substring(this.separator.length());
/* 2164 */           arity = arity.min(Math.max(1, arity.min));
/*      */         } 
/* 2166 */         if (arity.min > 0 && !CommandLine.empty(cluster) && 
/* 2167 */           CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Trying to process '%s' as option parameter%n", new Object[] { cluster });
/*      */ 
/*      */ 
/*      */ 
/*      */         
/* 2172 */         if (!CommandLine.empty(cluster)) {
/* 2173 */           args.push(cluster);
/*      */         }
/* 2175 */         int consumed = applyOption(field, CommandLine.Option.class, arity, paramAttachedToOption, args, initialized, argDescription);
/*      */         
/* 2177 */         if (CommandLine.empty(cluster) || consumed > 0 || args.isEmpty()) {
/*      */           return;
/*      */         }
/* 2180 */         cluster = args.pop();
/*      */       } 
/* 2182 */       if (cluster.length() == 0) {
/*      */         return;
/*      */       }
/*      */ 
/*      */       
/* 2187 */       if (arg.endsWith(cluster)) {
/* 2188 */         args.push(paramAttachedToOption ? (prefix + cluster) : cluster);
/* 2189 */         if (((String)args.peek()).equals(arg)) {
/* 2190 */           if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("Could not match any short options in %s, deciding whether to treat as unmatched option or positional parameter...%n", new Object[] { arg }); 
/* 2191 */           if (resemblesOption(arg)) { handleUnmatchedArguments(args.pop()); return; }
/* 2192 */            processPositionalParameter(required, initialized, args);
/*      */           
/*      */           return;
/*      */         } 
/* 2196 */         if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("No option found for %s in %s%n", new Object[] { cluster, arg }); 
/* 2197 */         handleUnmatchedArguments(args.pop());
/*      */       } else {
/* 2199 */         args.push(cluster);
/* 2200 */         if (CommandLine.this.tracer.isDebug()) CommandLine.this.tracer.debug("%s is not an option parameter for %s%n", new Object[] { cluster, arg }); 
/* 2201 */         processPositionalParameter(required, initialized, args);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int applyOption(Field field, Class<?> annotation, CommandLine.Range arity, boolean valueAttachedToOption, Stack<String> args, Set<Field> initialized, String argDescription) throws Exception {
/* 2215 */       updateHelpRequested(field);
/* 2216 */       int length = args.size();
/* 2217 */       assertNoMissingParameters(field, arity.min, args);
/*      */       
/* 2219 */       Class<?> cls = field.getType();
/* 2220 */       if (cls.isArray()) {
/* 2221 */         return applyValuesToArrayField(field, annotation, arity, args, cls, argDescription);
/*      */       }
/* 2223 */       if (Collection.class.isAssignableFrom(cls)) {
/* 2224 */         return applyValuesToCollectionField(field, annotation, arity, args, cls, argDescription);
/*      */       }
/* 2226 */       if (Map.class.isAssignableFrom(cls)) {
/* 2227 */         return applyValuesToMapField(field, annotation, arity, args, cls, argDescription);
/*      */       }
/* 2229 */       cls = CommandLine.getTypeAttribute(field)[0];
/* 2230 */       return applyValueToSingleValuedField(field, arity, args, cls, initialized, argDescription);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int applyValueToSingleValuedField(Field field, CommandLine.Range arity, Stack<String> args, Class<?> cls, Set<Field> initialized, String argDescription) throws Exception {
/* 2239 */       boolean noMoreValues = args.isEmpty();
/* 2240 */       String value = args.isEmpty() ? null : trim(args.pop());
/* 2241 */       int result = arity.min;
/*      */ 
/*      */       
/* 2244 */       if ((cls == Boolean.class || cls == boolean.class) && arity.min <= 0)
/*      */       {
/*      */         
/* 2247 */         if (arity.max > 0 && ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value))) {
/* 2248 */           result = 1;
/*      */         } else {
/* 2250 */           if (value != null) {
/* 2251 */             args.push(value);
/*      */           }
/* 2253 */           Boolean currentValue = (Boolean)field.get(this.command);
/* 2254 */           value = String.valueOf((currentValue == null) ? true : (!currentValue.booleanValue()));
/*      */         } 
/*      */       }
/* 2257 */       if (noMoreValues && value == null) {
/* 2258 */         return 0;
/*      */       }
/* 2260 */       CommandLine.ITypeConverter<?> converter = getTypeConverter(cls, field);
/* 2261 */       Object newValue = tryConvert(field, -1, converter, value, cls);
/* 2262 */       Object oldValue = field.get(this.command);
/* 2263 */       CommandLine.TraceLevel level = CommandLine.TraceLevel.INFO;
/* 2264 */       String traceMessage = "Setting %s field '%s.%s' to '%5$s' (was '%4$s') for %6$s%n";
/* 2265 */       if (initialized != null) {
/* 2266 */         if (initialized.contains(field)) {
/* 2267 */           if (!CommandLine.this.isOverwrittenOptionsAllowed()) {
/* 2268 */             throw new CommandLine.OverwrittenOptionException(CommandLine.this, optionDescription("", field, 0) + " should be specified only once");
/*      */           }
/* 2270 */           level = CommandLine.TraceLevel.WARN;
/* 2271 */           traceMessage = "Overwriting %s field '%s.%s' value '%s' with '%s' for %s%n";
/*      */         } 
/* 2273 */         initialized.add(field);
/*      */       } 
/* 2275 */       if (CommandLine.this.tracer.level.isEnabled(level)) level.print(CommandLine.this.tracer, traceMessage, new Object[] { field.getType().getSimpleName(), field
/* 2276 */               .getDeclaringClass().getSimpleName(), field.getName(), String.valueOf(oldValue), String.valueOf(newValue), argDescription });
/*      */       
/* 2278 */       field.set(this.command, newValue);
/* 2279 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int applyValuesToMapField(Field field, Class<?> annotation, CommandLine.Range arity, Stack<String> args, Class<?> cls, String argDescription) throws Exception {
/* 2287 */       Class<?>[] classes = CommandLine.getTypeAttribute(field);
/* 2288 */       if (classes.length < 2) throw new CommandLine.ParameterException(CommandLine.this, "Field " + field + " needs two types (one for the map key, one for the value) but only has " + classes.length + " types configured."); 
/* 2289 */       CommandLine.ITypeConverter<?> keyConverter = getTypeConverter(classes[0], field);
/* 2290 */       CommandLine.ITypeConverter<?> valueConverter = getTypeConverter(classes[1], field);
/* 2291 */       Map<Object, Object> result = (Map<Object, Object>)field.get(this.command);
/* 2292 */       if (result == null) {
/* 2293 */         result = createMap(cls);
/* 2294 */         field.set(this.command, result);
/*      */       } 
/* 2296 */       int originalSize = result.size();
/* 2297 */       consumeMapArguments(field, arity, args, classes, keyConverter, valueConverter, result, argDescription);
/* 2298 */       return result.size() - originalSize;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void consumeMapArguments(Field field, CommandLine.Range arity, Stack<String> args, Class<?>[] classes, CommandLine.ITypeConverter<?> keyConverter, CommandLine.ITypeConverter<?> valueConverter, Map<Object, Object> result, String argDescription) throws Exception {
/*      */       int i;
/* 2310 */       for (i = 0; i < arity.min; i++) {
/* 2311 */         consumeOneMapArgument(field, arity, args, classes, keyConverter, valueConverter, result, i, argDescription);
/*      */       }
/*      */       
/* 2314 */       for (i = arity.min; i < arity.max && !args.isEmpty(); i++) {
/* 2315 */         if (!field.isAnnotationPresent((Class)CommandLine.Parameters.class) && (
/* 2316 */           this.commands.containsKey(args.peek()) || isOption(args.peek()))) {
/*      */           return;
/*      */         }
/*      */         
/* 2320 */         consumeOneMapArgument(field, arity, args, classes, keyConverter, valueConverter, result, i, argDescription);
/*      */       } 
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private void consumeOneMapArgument(Field field, CommandLine.Range arity, Stack<String> args, Class<?>[] classes, CommandLine.ITypeConverter<?> keyConverter, CommandLine.ITypeConverter<?> valueConverter, Map<Object, Object> result, int index, String argDescription) throws Exception {
/* 2332 */       String[] values = split(trim(args.pop()), field);
/* 2333 */       for (String value : values) {
/* 2334 */         String[] keyValue = value.split("=");
/* 2335 */         if (keyValue.length < 2) {
/* 2336 */           String splitRegex = splitRegex(field);
/* 2337 */           if (splitRegex.length() == 0) {
/* 2338 */             throw new CommandLine.ParameterException(CommandLine.this, "Value for option " + optionDescription("", field, 0) + " should be in KEY=VALUE format but was " + value);
/*      */           }
/*      */           
/* 2341 */           throw new CommandLine.ParameterException(CommandLine.this, "Value for option " + optionDescription("", field, 0) + " should be in KEY=VALUE[" + splitRegex + "KEY=VALUE]... format but was " + value);
/*      */         } 
/*      */ 
/*      */         
/* 2345 */         Object mapKey = tryConvert(field, index, keyConverter, keyValue[0], classes[0]);
/* 2346 */         Object mapValue = tryConvert(field, index, valueConverter, keyValue[1], classes[1]);
/* 2347 */         result.put(mapKey, mapValue);
/* 2348 */         if (CommandLine.this.tracer.isInfo()) CommandLine.this.tracer.info("Putting [%s : %s] in %s<%s, %s> field '%s.%s' for %s%n", new Object[] { String.valueOf(mapKey), String.valueOf(mapValue), result
/* 2349 */                 .getClass().getSimpleName(), classes[0].getSimpleName(), classes[1].getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), argDescription }); 
/*      */       } 
/*      */     }
/*      */     
/*      */     private void checkMaxArityExceeded(CommandLine.Range arity, int remainder, Field field, String[] values) {
/* 2354 */       if (values.length <= remainder)
/* 2355 */         return;  String desc = (arity.max == remainder) ? ("" + remainder) : (arity + ", remainder=" + remainder);
/* 2356 */       throw new CommandLine.MaxValuesforFieldExceededException(CommandLine.this, optionDescription("", field, -1) + " max number of values (" + arity.max + ") exceeded: remainder is " + remainder + " but " + values.length + " values were specified: " + 
/*      */           
/* 2358 */           Arrays.toString(values));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int applyValuesToArrayField(Field field, Class<?> annotation, CommandLine.Range arity, Stack<String> args, Class<?> cls, String argDescription) throws Exception {
/* 2367 */       Object existing = field.get(this.command);
/* 2368 */       int length = (existing == null) ? 0 : Array.getLength(existing);
/* 2369 */       Class<?> type = CommandLine.getTypeAttribute(field)[0];
/* 2370 */       List<Object> converted = consumeArguments(field, annotation, arity, args, type, length, argDescription);
/* 2371 */       List<Object> newValues = new ArrayList();
/* 2372 */       for (int i = 0; i < length; i++) {
/* 2373 */         newValues.add(Array.get(existing, i));
/*      */       }
/* 2375 */       for (Object obj : converted) {
/* 2376 */         if (obj instanceof Collection) {
/* 2377 */           newValues.addAll((Collection)obj); continue;
/*      */         } 
/* 2379 */         newValues.add(obj);
/*      */       } 
/*      */       
/* 2382 */       Object array = Array.newInstance(type, newValues.size());
/* 2383 */       field.set(this.command, array);
/* 2384 */       for (int j = 0; j < newValues.size(); j++) {
/* 2385 */         Array.set(array, j, newValues.get(j));
/*      */       }
/* 2387 */       return converted.size();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int applyValuesToCollectionField(Field field, Class<?> annotation, CommandLine.Range arity, Stack<String> args, Class<?> cls, String argDescription) throws Exception {
/* 2397 */       Collection<Object> collection = (Collection<Object>)field.get(this.command);
/* 2398 */       Class<?> type = CommandLine.getTypeAttribute(field)[0];
/* 2399 */       int length = (collection == null) ? 0 : collection.size();
/* 2400 */       List<Object> converted = consumeArguments(field, annotation, arity, args, type, length, argDescription);
/* 2401 */       if (collection == null) {
/* 2402 */         collection = createCollection(cls);
/* 2403 */         field.set(this.command, collection);
/*      */       } 
/* 2405 */       for (Object element : converted) {
/* 2406 */         if (element instanceof Collection) {
/* 2407 */           collection.addAll((Collection)element); continue;
/*      */         } 
/* 2409 */         collection.add(element);
/*      */       } 
/*      */       
/* 2412 */       return converted.size();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private List<Object> consumeArguments(Field field, Class<?> annotation, CommandLine.Range arity, Stack<String> args, Class<?> type, int originalSize, String argDescription) throws Exception {
/* 2422 */       List<Object> result = new ArrayList();
/*      */       
/*      */       int i;
/* 2425 */       for (i = 0; i < arity.min; i++) {
/* 2426 */         consumeOneArgument(field, arity, args, type, result, i, originalSize, argDescription);
/*      */       }
/*      */       
/* 2429 */       for (i = arity.min; i < arity.max && !args.isEmpty(); i++) {
/* 2430 */         if (annotation != CommandLine.Parameters.class && (
/* 2431 */           this.commands.containsKey(args.peek()) || isOption(args.peek()))) {
/* 2432 */           return result;
/*      */         }
/*      */         
/* 2435 */         consumeOneArgument(field, arity, args, type, result, i, originalSize, argDescription);
/*      */       } 
/* 2437 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private int consumeOneArgument(Field field, CommandLine.Range arity, Stack<String> args, Class<?> type, List<Object> result, int index, int originalSize, String argDescription) throws Exception {
/* 2448 */       String[] values = split(trim(args.pop()), field);
/* 2449 */       CommandLine.ITypeConverter<?> converter = getTypeConverter(type, field);
/*      */       
/* 2451 */       for (int j = 0; j < values.length; j++) {
/* 2452 */         result.add(tryConvert(field, index, converter, values[j], type));
/* 2453 */         if (CommandLine.this.tracer.isInfo()) {
/* 2454 */           if (field.getType().isArray()) {
/* 2455 */             CommandLine.this.tracer.info("Adding [%s] to %s[] field '%s.%s' for %s%n", new Object[] { String.valueOf(result.get(result.size() - 1)), type.getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), argDescription });
/*      */           } else {
/* 2457 */             CommandLine.this.tracer.info("Adding [%s] to %s<%s> field '%s.%s' for %s%n", new Object[] { String.valueOf(result.get(result.size() - 1)), field.getType().getSimpleName(), type.getSimpleName(), field.getDeclaringClass().getSimpleName(), field.getName(), argDescription });
/*      */           } 
/*      */         }
/*      */       } 
/*      */       
/* 2462 */       return ++index;
/*      */     }
/*      */     
/*      */     private String splitRegex(Field field) {
/* 2466 */       if (field.isAnnotationPresent((Class)CommandLine.Option.class)) return ((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).split(); 
/* 2467 */       if (field.isAnnotationPresent((Class)CommandLine.Parameters.class)) return ((CommandLine.Parameters)field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).split(); 
/* 2468 */       return "";
/*      */     }
/*      */     private String[] split(String value, Field field) {
/* 2471 */       String regex = splitRegex(field);
/* 2472 */       (new String[1])[0] = value; return (regex.length() == 0) ? new String[1] : value.split(regex);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private boolean isOption(String arg) {
/* 2482 */       if ("--".equals(arg)) {
/* 2483 */         return true;
/*      */       }
/*      */       
/* 2486 */       if (this.optionName2Field.containsKey(arg)) {
/* 2487 */         return true;
/*      */       }
/* 2489 */       int separatorIndex = arg.indexOf(this.separator);
/* 2490 */       if (separatorIndex > 0 && 
/* 2491 */         this.optionName2Field.containsKey(arg.substring(0, separatorIndex))) {
/* 2492 */         return true;
/*      */       }
/*      */       
/* 2495 */       return (arg.length() > 2 && arg.startsWith("-") && this.singleCharOption2Field.containsKey(Character.valueOf(arg.charAt(1))));
/*      */     }
/*      */     
/*      */     private Object tryConvert(Field field, int index, CommandLine.ITypeConverter<?> converter, String value, Class<?> type) throws Exception {
/*      */       try {
/* 2500 */         return converter.convert(value);
/* 2501 */       } catch (TypeConversionException ex) {
/* 2502 */         throw new CommandLine.ParameterException(CommandLine.this, ex.getMessage() + optionDescription(" for ", field, index));
/* 2503 */       } catch (Exception other) {
/* 2504 */         String desc = optionDescription(" for ", field, index) + ": " + other;
/* 2505 */         throw new CommandLine.ParameterException(CommandLine.this, "Could not convert '" + value + "' to " + type.getSimpleName() + desc, other);
/*      */       } 
/*      */     }
/*      */     
/*      */     private String optionDescription(String prefix, Field field, int index) {
/* 2510 */       CommandLine.Help.IParamLabelRenderer labelRenderer = CommandLine.Help.createMinimalParamLabelRenderer();
/* 2511 */       String desc = "";
/* 2512 */       if (field.isAnnotationPresent((Class)CommandLine.Option.class)) {
/* 2513 */         desc = prefix + "option '" + ((CommandLine.Option)field.getAnnotation((Class)CommandLine.Option.class)).names()[0] + "'";
/* 2514 */         if (index >= 0) {
/* 2515 */           CommandLine.Range arity = CommandLine.Range.optionArity(field);
/* 2516 */           if (arity.max > 1) {
/* 2517 */             desc = desc + " at index " + index;
/*      */           }
/* 2519 */           desc = desc + " (" + labelRenderer.renderParameterLabel(field, CommandLine.Help.Ansi.OFF, Collections.emptyList()) + ")";
/*      */         } 
/* 2521 */       } else if (field.isAnnotationPresent((Class)CommandLine.Parameters.class)) {
/* 2522 */         CommandLine.Range indexRange = CommandLine.Range.parameterIndex(field);
/* 2523 */         CommandLine.Help.Ansi.Text label = labelRenderer.renderParameterLabel(field, CommandLine.Help.Ansi.OFF, Collections.emptyList());
/* 2524 */         desc = prefix + "positional parameter at index " + indexRange + " (" + label + ")";
/*      */       } 
/* 2526 */       return desc;
/*      */     }
/*      */     private boolean isAnyHelpRequested() {
/* 2529 */       return (this.isHelpRequested || CommandLine.this.versionHelpRequested || CommandLine.this.usageHelpRequested);
/*      */     }
/*      */     private void updateHelpRequested(Field field) {
/* 2532 */       if (field.isAnnotationPresent((Class)CommandLine.Option.class)) {
/* 2533 */         this.isHelpRequested |= is(field, "help", ((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).help());
/* 2534 */         CommandLine commandLine = CommandLine.this; commandLine.versionHelpRequested = commandLine.versionHelpRequested | is(field, "versionHelp", ((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).versionHelp());
/* 2535 */         commandLine = CommandLine.this; commandLine.usageHelpRequested = commandLine.usageHelpRequested | is(field, "usageHelp", ((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).usageHelp());
/*      */       } 
/*      */     }
/*      */     private boolean is(Field f, String description, boolean value) {
/* 2539 */       if (value && CommandLine.this.tracer.isInfo()) CommandLine.this.tracer.info("Field '%s.%s' has '%s' annotation: not validating required fields%n", new Object[] { f.getDeclaringClass().getSimpleName(), f.getName(), description }); 
/* 2540 */       return value;
/*      */     }
/*      */     
/*      */     private Collection<Object> createCollection(Class<?> collectionClass) throws Exception {
/* 2544 */       if (collectionClass.isInterface()) {
/* 2545 */         if (SortedSet.class.isAssignableFrom(collectionClass))
/* 2546 */           return new TreeSet(); 
/* 2547 */         if (Set.class.isAssignableFrom(collectionClass))
/* 2548 */           return new LinkedHashSet(); 
/* 2549 */         if (Queue.class.isAssignableFrom(collectionClass)) {
/* 2550 */           return new LinkedList();
/*      */         }
/* 2552 */         return new ArrayList();
/*      */       } 
/*      */       
/* 2555 */       return (Collection<Object>)collectionClass.newInstance();
/*      */     }
/*      */     private Map<Object, Object> createMap(Class<?> mapClass) throws Exception {
/*      */       try {
/* 2559 */         return (Map<Object, Object>)mapClass.newInstance();
/* 2560 */       } catch (Exception exception) {
/* 2561 */         return new LinkedHashMap<>();
/*      */       } 
/*      */     } private CommandLine.ITypeConverter<?> getTypeConverter(final Class<?> type, Field field) {
/* 2564 */       CommandLine.ITypeConverter<?> result = this.converterRegistry.get(type);
/* 2565 */       if (result != null) {
/* 2566 */         return result;
/*      */       }
/* 2568 */       if (type.isEnum()) {
/* 2569 */         return new CommandLine.ITypeConverter()
/*      */           {
/*      */             public Object convert(String value) throws Exception
/*      */             {
/* 2573 */               return Enum.valueOf(type, value);
/*      */             }
/*      */           };
/*      */       }
/* 2577 */       throw new CommandLine.MissingTypeConverterException(CommandLine.this, "No TypeConverter registered for " + type.getName() + " of field " + field);
/*      */     }
/*      */     
/*      */     private void assertNoMissingParameters(Field field, int arity, Stack<String> args) {
/* 2581 */       if (arity > args.size()) {
/* 2582 */         if (arity == 1) {
/* 2583 */           if (field.isAnnotationPresent((Class)CommandLine.Option.class)) {
/* 2584 */             throw new CommandLine.MissingParameterException(CommandLine.this, "Missing required parameter for " + 
/* 2585 */                 optionDescription("", field, 0));
/*      */           }
/* 2587 */           CommandLine.Range indexRange = CommandLine.Range.parameterIndex(field);
/* 2588 */           CommandLine.Help.IParamLabelRenderer labelRenderer = CommandLine.Help.createMinimalParamLabelRenderer();
/* 2589 */           String sep = "";
/* 2590 */           String names = "";
/* 2591 */           int count = 0;
/* 2592 */           for (int i = indexRange.min; i < this.positionalParametersFields.size(); i++) {
/* 2593 */             if ((CommandLine.Range.parameterArity((Field)this.positionalParametersFields.get(i))).min > 0) {
/* 2594 */               names = names + sep + labelRenderer.renderParameterLabel(this.positionalParametersFields.get(i), CommandLine.Help.Ansi.OFF, 
/* 2595 */                   Collections.emptyList());
/* 2596 */               sep = ", ";
/* 2597 */               count++;
/*      */             } 
/*      */           } 
/* 2600 */           String msg = "Missing required parameter";
/* 2601 */           CommandLine.Range paramArity = CommandLine.Range.parameterArity(field);
/* 2602 */           if (paramArity.isVariable) {
/* 2603 */             msg = msg + "s at positions " + indexRange + ": ";
/*      */           } else {
/* 2605 */             msg = msg + ((count > 1) ? "s: " : ": ");
/*      */           } 
/* 2607 */           throw new CommandLine.MissingParameterException(CommandLine.this, msg + names);
/*      */         } 
/* 2609 */         if (args.isEmpty()) {
/* 2610 */           throw new CommandLine.MissingParameterException(CommandLine.this, optionDescription("", field, 0) + " requires at least " + arity + " values, but none were specified.");
/*      */         }
/*      */         
/* 2613 */         throw new CommandLine.MissingParameterException(CommandLine.this, optionDescription("", field, 0) + " requires at least " + arity + " values, but only " + args
/* 2614 */             .size() + " were specified: " + CommandLine.reverse(args));
/*      */       } 
/*      */     }
/*      */     private String trim(String value) {
/* 2618 */       return unquote(value);
/*      */     }
/*      */     
/*      */     private String unquote(String value) {
/* 2622 */       return (value == null) ? null : ((value
/*      */         
/* 2624 */         .length() > 1 && value.startsWith("\"") && value.endsWith("\"")) ? value
/* 2625 */         .substring(1, value.length() - 1) : value);
/*      */     } }
/*      */   
/*      */   private static class PositionalParametersSorter implements Comparator<Field> {
/*      */     private PositionalParametersSorter() {}
/*      */     
/*      */     public int compare(Field o1, Field o2) {
/* 2632 */       int result = CommandLine.Range.parameterIndex(o1).compareTo(CommandLine.Range.parameterIndex(o2));
/* 2633 */       return (result == 0) ? CommandLine.Range.parameterArity(o1).compareTo(CommandLine.Range.parameterArity(o2)) : result;
/*      */     }
/*      */   }
/*      */   
/*      */   private static class BuiltIn {
/*      */     static class PathConverter
/*      */       implements CommandLine.ITypeConverter<Path> {
/*      */       public Path convert(String value) {
/* 2641 */         return Paths.get(value, new String[0]);
/*      */       } }
/*      */     
/*      */     static class StringConverter implements CommandLine.ITypeConverter<String> { public String convert(String value) {
/* 2645 */         return value;
/*      */       } }
/*      */     
/*      */     static class StringBuilderConverter implements CommandLine.ITypeConverter<StringBuilder> { public StringBuilder convert(String value) {
/* 2649 */         return new StringBuilder(value);
/*      */       } }
/*      */     
/*      */     static class CharSequenceConverter implements CommandLine.ITypeConverter<CharSequence> { public String convert(String value) {
/* 2653 */         return value;
/*      */       } }
/*      */     
/*      */     static class ByteConverter implements CommandLine.ITypeConverter<Byte> {
/*      */       public Byte convert(String value) {
/* 2658 */         return Byte.valueOf(value);
/*      */       }
/*      */     }
/*      */     
/*      */     static class BooleanConverter implements CommandLine.ITypeConverter<Boolean> {
/*      */       public Boolean convert(String value) {
/* 2664 */         if ("true".equalsIgnoreCase(value) || "false".equalsIgnoreCase(value)) {
/* 2665 */           return Boolean.valueOf(Boolean.parseBoolean(value));
/*      */         }
/* 2667 */         throw new CommandLine.TypeConversionException("'" + value + "' is not a boolean");
/*      */       }
/*      */     }
/*      */     
/*      */     static class CharacterConverter
/*      */       implements CommandLine.ITypeConverter<Character> {
/*      */       public Character convert(String value) {
/* 2674 */         if (value.length() > 1) {
/* 2675 */           throw new CommandLine.TypeConversionException("'" + value + "' is not a single character");
/*      */         }
/* 2677 */         return Character.valueOf(value.charAt(0));
/*      */       }
/*      */     }
/*      */     
/*      */     static class ShortConverter implements CommandLine.ITypeConverter<Short> {
/*      */       public Short convert(String value) {
/* 2683 */         return Short.valueOf(value);
/*      */       } }
/*      */     
/*      */     static class IntegerConverter implements CommandLine.ITypeConverter<Integer> {
/*      */       public Integer convert(String value) {
/* 2688 */         return Integer.valueOf(value);
/*      */       }
/*      */     }
/*      */     
/*      */     static class LongConverter implements CommandLine.ITypeConverter<Long> { public Long convert(String value) {
/* 2693 */         return Long.valueOf(value);
/*      */       } }
/*      */     
/*      */     static class FloatConverter implements CommandLine.ITypeConverter<Float> { public Float convert(String value) {
/* 2697 */         return Float.valueOf(value);
/*      */       } }
/*      */     
/*      */     static class DoubleConverter implements CommandLine.ITypeConverter<Double> { public Double convert(String value) {
/* 2701 */         return Double.valueOf(value);
/*      */       } }
/*      */     
/*      */     static class FileConverter implements CommandLine.ITypeConverter<File> { public File convert(String value) {
/* 2705 */         return new File(value);
/*      */       } }
/*      */     
/*      */     static class URLConverter implements CommandLine.ITypeConverter<URL> { public URL convert(String value) throws MalformedURLException {
/* 2709 */         return new URL(value);
/*      */       } }
/*      */     
/*      */     static class URIConverter implements CommandLine.ITypeConverter<URI> { public URI convert(String value) throws URISyntaxException {
/* 2713 */         return new URI(value);
/*      */       } }
/*      */ 
/*      */     
/*      */     static class ISO8601DateConverter implements CommandLine.ITypeConverter<Date> {
/*      */       public Date convert(String value) {
/*      */         try {
/* 2720 */           return (new SimpleDateFormat("yyyy-MM-dd")).parse(value);
/* 2721 */         } catch (ParseException e) {
/* 2722 */           throw new CommandLine.TypeConversionException("'" + value + "' is not a yyyy-MM-dd date");
/*      */         } 
/*      */       }
/*      */     }
/*      */     
/*      */     static class ISO8601TimeConverter
/*      */       implements CommandLine.ITypeConverter<Time>
/*      */     {
/*      */       public Time convert(String value) {
/*      */         try {
/* 2732 */           if (value.length() <= 5)
/* 2733 */             return new Time((new SimpleDateFormat("HH:mm")).parse(value).getTime()); 
/* 2734 */           if (value.length() <= 8)
/* 2735 */             return new Time((new SimpleDateFormat("HH:mm:ss")).parse(value).getTime()); 
/* 2736 */           if (value.length() <= 12) {
/*      */             try {
/* 2738 */               return new Time((new SimpleDateFormat("HH:mm:ss.SSS")).parse(value).getTime());
/* 2739 */             } catch (ParseException e2) {
/* 2740 */               return new Time((new SimpleDateFormat("HH:mm:ss,SSS")).parse(value).getTime());
/*      */             } 
/*      */           }
/* 2743 */         } catch (ParseException parseException) {}
/*      */ 
/*      */         
/* 2746 */         throw new CommandLine.TypeConversionException("'" + value + "' is not a HH:mm[:ss[.SSS]] time");
/*      */       }
/*      */     }
/*      */     
/*      */     static class BigDecimalConverter implements CommandLine.ITypeConverter<BigDecimal> { public BigDecimal convert(String value) {
/* 2751 */         return new BigDecimal(value);
/*      */       } }
/*      */     
/*      */     static class BigIntegerConverter implements CommandLine.ITypeConverter<BigInteger> { public BigInteger convert(String value) {
/* 2755 */         return new BigInteger(value);
/*      */       } }
/*      */     
/*      */     static class CharsetConverter implements CommandLine.ITypeConverter<Charset> { public Charset convert(String s) {
/* 2759 */         return Charset.forName(s);
/*      */       } }
/*      */ 
/*      */     
/*      */     static class InetAddressConverter implements CommandLine.ITypeConverter<InetAddress> { public InetAddress convert(String s) throws Exception {
/* 2764 */         return InetAddress.getByName(s);
/*      */       } }
/*      */     
/*      */     static class PatternConverter implements CommandLine.ITypeConverter<Pattern> { public Pattern convert(String s) {
/* 2768 */         return Pattern.compile(s);
/*      */       } }
/*      */     
/*      */     static class UUIDConverter implements CommandLine.ITypeConverter<UUID> { public UUID convert(String s) throws Exception {
/* 2772 */         return UUID.fromString(s);
/*      */       } }
/*      */   
/*      */   }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   public static class Help
/*      */   {
/*      */     protected static final String DEFAULT_COMMAND_NAME = "<main class>";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     protected static final String DEFAULT_SEPARATOR = "=";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final int usageHelpWidth = 80;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private static final int optionsColumnWidth = 29;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     private final Object command;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2820 */     private final Map<String, Help> commands = new LinkedHashMap<>();
/*      */ 
/*      */ 
/*      */     
/*      */     final ColorScheme colorScheme;
/*      */ 
/*      */     
/*      */     public final List<Field> optionFields;
/*      */ 
/*      */     
/*      */     public final List<Field> positionalParametersFields;
/*      */ 
/*      */     
/*      */     public String separator;
/*      */ 
/*      */     
/* 2836 */     public String commandName = "<main class>";
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2842 */     public String[] description = new String[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2848 */     public String[] customSynopsis = new String[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2854 */     public String[] header = new String[0];
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/* 2860 */     public String[] footer = new String[0];
/*      */ 
/*      */ 
/*      */     
/*      */     public IParamLabelRenderer parameterLabelRenderer;
/*      */ 
/*      */ 
/*      */     
/*      */     public Boolean abbreviateSynopsis;
/*      */ 
/*      */ 
/*      */     
/*      */     public Boolean sortOptions;
/*      */ 
/*      */ 
/*      */     
/*      */     public Boolean showDefaultValues;
/*      */ 
/*      */     
/*      */     public Character requiredOptionMarker;
/*      */ 
/*      */     
/*      */     public String headerHeading;
/*      */ 
/*      */     
/*      */     public String synopsisHeading;
/*      */ 
/*      */     
/*      */     public String descriptionHeading;
/*      */ 
/*      */     
/*      */     public String parameterListHeading;
/*      */ 
/*      */     
/*      */     public String optionListHeading;
/*      */ 
/*      */     
/*      */     public String commandListHeading;
/*      */ 
/*      */     
/*      */     public String footerHeading;
/*      */ 
/*      */ 
/*      */     
/*      */     public Help(Object command) {
/* 2905 */       this(command, Ansi.AUTO);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Help(Object command, Ansi ansi) {
/* 2913 */       this(command, defaultColorScheme(ansi));
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Help(Object command, ColorScheme colorScheme) {
/* 2921 */       this.command = CommandLine.Assert.notNull(command, "command");
/* 2922 */       this.colorScheme = ((ColorScheme)CommandLine.Assert.<ColorScheme>notNull(colorScheme, "colorScheme")).applySystemProperties();
/* 2923 */       List<Field> options = new ArrayList<>();
/* 2924 */       List<Field> operands = new ArrayList<>();
/* 2925 */       Class<?> cls = command.getClass();
/* 2926 */       while (cls != null) {
/* 2927 */         for (Field field : cls.getDeclaredFields()) {
/* 2928 */           field.setAccessible(true);
/* 2929 */           if (field.isAnnotationPresent((Class)CommandLine.Option.class)) {
/* 2930 */             CommandLine.Option option = field.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
/* 2931 */             if (!option.hidden())
/*      */             {
/* 2933 */               options.add(field);
/*      */             }
/*      */           } 
/* 2936 */           if (field.isAnnotationPresent((Class)CommandLine.Parameters.class)) {
/* 2937 */             operands.add(field);
/*      */           }
/*      */         } 
/*      */         
/* 2941 */         if (cls.isAnnotationPresent((Class)CommandLine.Command.class)) {
/* 2942 */           CommandLine.Command cmd = cls.<CommandLine.Command>getAnnotation(CommandLine.Command.class);
/* 2943 */           if ("<main class>".equals(this.commandName)) {
/* 2944 */             this.commandName = cmd.name();
/*      */           }
/* 2946 */           this.separator = (this.separator == null) ? cmd.separator() : this.separator;
/* 2947 */           this.abbreviateSynopsis = Boolean.valueOf((this.abbreviateSynopsis == null) ? cmd.abbreviateSynopsis() : this.abbreviateSynopsis.booleanValue());
/* 2948 */           this.sortOptions = Boolean.valueOf((this.sortOptions == null) ? cmd.sortOptions() : this.sortOptions.booleanValue());
/* 2949 */           this.requiredOptionMarker = Character.valueOf((this.requiredOptionMarker == null) ? cmd.requiredOptionMarker() : this.requiredOptionMarker.charValue());
/* 2950 */           this.showDefaultValues = Boolean.valueOf((this.showDefaultValues == null) ? cmd.showDefaultValues() : this.showDefaultValues.booleanValue());
/* 2951 */           this.customSynopsis = CommandLine.empty((Object[])this.customSynopsis) ? cmd.customSynopsis() : this.customSynopsis;
/* 2952 */           this.description = CommandLine.empty((Object[])this.description) ? cmd.description() : this.description;
/* 2953 */           this.header = CommandLine.empty((Object[])this.header) ? cmd.header() : this.header;
/* 2954 */           this.footer = CommandLine.empty((Object[])this.footer) ? cmd.footer() : this.footer;
/* 2955 */           this.headerHeading = CommandLine.empty(this.headerHeading) ? cmd.headerHeading() : this.headerHeading;
/* 2956 */           this.synopsisHeading = (CommandLine.empty(this.synopsisHeading) || "Usage: ".equals(this.synopsisHeading)) ? cmd.synopsisHeading() : this.synopsisHeading;
/* 2957 */           this.descriptionHeading = CommandLine.empty(this.descriptionHeading) ? cmd.descriptionHeading() : this.descriptionHeading;
/* 2958 */           this.parameterListHeading = CommandLine.empty(this.parameterListHeading) ? cmd.parameterListHeading() : this.parameterListHeading;
/* 2959 */           this.optionListHeading = CommandLine.empty(this.optionListHeading) ? cmd.optionListHeading() : this.optionListHeading;
/* 2960 */           this.commandListHeading = (CommandLine.empty(this.commandListHeading) || "Commands:%n".equals(this.commandListHeading)) ? cmd.commandListHeading() : this.commandListHeading;
/* 2961 */           this.footerHeading = CommandLine.empty(this.footerHeading) ? cmd.footerHeading() : this.footerHeading;
/*      */         } 
/* 2963 */         cls = cls.getSuperclass();
/*      */       } 
/* 2965 */       this.sortOptions = Boolean.valueOf((this.sortOptions == null) ? true : this.sortOptions.booleanValue());
/* 2966 */       this.abbreviateSynopsis = Boolean.valueOf((this.abbreviateSynopsis == null) ? false : this.abbreviateSynopsis.booleanValue());
/* 2967 */       this.requiredOptionMarker = Character.valueOf((this.requiredOptionMarker == null) ? 32 : this.requiredOptionMarker.charValue());
/* 2968 */       this.showDefaultValues = Boolean.valueOf((this.showDefaultValues == null) ? false : this.showDefaultValues.booleanValue());
/* 2969 */       this.synopsisHeading = (this.synopsisHeading == null) ? "Usage: " : this.synopsisHeading;
/* 2970 */       this.commandListHeading = (this.commandListHeading == null) ? "Commands:%n" : this.commandListHeading;
/* 2971 */       this.separator = (this.separator == null) ? "=" : this.separator;
/* 2972 */       this.parameterLabelRenderer = createDefaultParamLabelRenderer();
/* 2973 */       Collections.sort(operands, new CommandLine.PositionalParametersSorter());
/* 2974 */       this.positionalParametersFields = Collections.unmodifiableList(operands);
/* 2975 */       this.optionFields = Collections.unmodifiableList(options);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Help addAllSubcommands(Map<String, CommandLine> commands) {
/* 2984 */       if (commands != null) {
/* 2985 */         for (Map.Entry<String, CommandLine> entry : commands.entrySet()) {
/* 2986 */           addSubcommand(entry.getKey(), ((CommandLine)entry.getValue()).getCommand());
/*      */         }
/*      */       }
/* 2989 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Help addSubcommand(String commandName, Object command) {
/* 2998 */       this.commands.put(commandName, new Help(command));
/* 2999 */       return this;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public String synopsis() {
/* 3009 */       return synopsis(0);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String synopsis(int synopsisHeadingLength) {
/* 3020 */       if (!CommandLine.empty((Object[])this.customSynopsis)) return customSynopsis(new Object[0]); 
/* 3021 */       return this.abbreviateSynopsis.booleanValue() ? abbreviatedSynopsis() : 
/* 3022 */         detailedSynopsis(synopsisHeadingLength, createShortOptionArityAndNameComparator(), true);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String abbreviatedSynopsis() {
/* 3029 */       StringBuilder sb = new StringBuilder();
/* 3030 */       if (!this.optionFields.isEmpty()) {
/* 3031 */         sb.append(" [OPTIONS]");
/*      */       }
/*      */       
/* 3034 */       for (Field positionalParam : this.positionalParametersFields) {
/* 3035 */         if (!((CommandLine.Parameters)positionalParam.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).hidden()) {
/* 3036 */           sb.append(' ').append(this.parameterLabelRenderer.renderParameterLabel(positionalParam, ansi(), this.colorScheme.parameterStyles));
/*      */         }
/*      */       } 
/* 3039 */       return this.colorScheme.commandText(this.commandName).toString() + sb
/* 3040 */         .toString() + System.getProperty("line.separator");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     @Deprecated
/*      */     public String detailedSynopsis(Comparator<Field> optionSort, boolean clusterBooleanOptions) {
/* 3050 */       return detailedSynopsis(0, optionSort, clusterBooleanOptions);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String detailedSynopsis(int synopsisHeadingLength, Comparator<Field> optionSort, boolean clusterBooleanOptions) {
/* 3060 */       ansi().getClass(); Ansi.Text optionText = new Ansi.Text(0);
/* 3061 */       List<Field> fields = new ArrayList<>(this.optionFields);
/* 3062 */       if (optionSort != null) {
/* 3063 */         Collections.sort(fields, optionSort);
/*      */       }
/* 3065 */       if (clusterBooleanOptions) {
/* 3066 */         List<Field> booleanOptions = new ArrayList<>();
/* 3067 */         StringBuilder clusteredRequired = new StringBuilder("-");
/* 3068 */         StringBuilder clusteredOptional = new StringBuilder("-");
/* 3069 */         for (Field field : fields) {
/* 3070 */           if (field.getType() == boolean.class || field.getType() == Boolean.class) {
/* 3071 */             CommandLine.Option option = field.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
/* 3072 */             String shortestName = ShortestFirst.sort(option.names())[0];
/* 3073 */             if (shortestName.length() == 2 && shortestName.startsWith("-")) {
/* 3074 */               booleanOptions.add(field);
/* 3075 */               if (option.required()) {
/* 3076 */                 clusteredRequired.append(shortestName.substring(1)); continue;
/*      */               } 
/* 3078 */               clusteredOptional.append(shortestName.substring(1));
/*      */             } 
/*      */           } 
/*      */         } 
/*      */         
/* 3083 */         fields.removeAll(booleanOptions);
/* 3084 */         if (clusteredRequired.length() > 1) {
/* 3085 */           optionText = optionText.append(" ").append(this.colorScheme.optionText(clusteredRequired.toString()));
/*      */         }
/* 3087 */         if (clusteredOptional.length() > 1) {
/* 3088 */           optionText = optionText.append(" [").append(this.colorScheme.optionText(clusteredOptional.toString())).append("]");
/*      */         }
/*      */       } 
/* 3091 */       for (Field field : fields) {
/* 3092 */         CommandLine.Option option = field.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
/* 3093 */         if (!option.hidden()) {
/* 3094 */           if (option.required()) {
/* 3095 */             optionText = appendOptionSynopsis(optionText, field, ShortestFirst.sort(option.names())[0], " ", "");
/* 3096 */             if (CommandLine.isMultiValue(field))
/* 3097 */               optionText = appendOptionSynopsis(optionText, field, ShortestFirst.sort(option.names())[0], " [", "]..."); 
/*      */             continue;
/*      */           } 
/* 3100 */           optionText = appendOptionSynopsis(optionText, field, ShortestFirst.sort(option.names())[0], " [", "]");
/* 3101 */           if (CommandLine.isMultiValue(field)) {
/* 3102 */             optionText = optionText.append("...");
/*      */           }
/*      */         } 
/*      */       } 
/*      */       
/* 3107 */       for (Field positionalParam : this.positionalParametersFields) {
/* 3108 */         if (!((CommandLine.Parameters)positionalParam.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).hidden()) {
/* 3109 */           optionText = optionText.append(" ");
/* 3110 */           Ansi.Text label = this.parameterLabelRenderer.renderParameterLabel(positionalParam, this.colorScheme.ansi(), this.colorScheme.parameterStyles);
/* 3111 */           optionText = optionText.append(label);
/*      */         } 
/*      */       } 
/*      */       
/* 3115 */       int firstColumnLength = this.commandName.length() + synopsisHeadingLength;
/*      */ 
/*      */       
/* 3118 */       TextTable textTable = new TextTable(ansi(), new int[] { firstColumnLength, 80 - firstColumnLength });
/* 3119 */       textTable.indentWrappedLines = 1;
/*      */ 
/*      */       
/* 3122 */       Ansi.OFF.getClass(); Ansi.Text PADDING = new Ansi.Text(stringOf('X', synopsisHeadingLength));
/* 3123 */       textTable.addRowValues(new Ansi.Text[] { PADDING.append(this.colorScheme.commandText(this.commandName)), optionText });
/* 3124 */       return textTable.toString().substring(synopsisHeadingLength);
/*      */     }
/*      */     
/*      */     private Ansi.Text appendOptionSynopsis(Ansi.Text optionText, Field field, String optionName, String prefix, String suffix) {
/* 3128 */       Ansi.Text optionParamText = this.parameterLabelRenderer.renderParameterLabel(field, this.colorScheme.ansi(), this.colorScheme.optionParamStyles);
/* 3129 */       return optionText.append(prefix)
/* 3130 */         .append(this.colorScheme.optionText(optionName))
/* 3131 */         .append(optionParamText)
/* 3132 */         .append(suffix);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public int synopsisHeadingLength() {
/* 3140 */       Ansi.OFF.getClass(); String[] lines = (new Ansi.Text(this.synopsisHeading)).toString().split("\\r?\\n|\\r|%n", -1);
/* 3141 */       return lines[lines.length - 1].length();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String optionList() {
/* 3153 */       Comparator<Field> sortOrder = (this.sortOptions == null || this.sortOptions.booleanValue()) ? createShortOptionNameComparator() : null;
/*      */       
/* 3155 */       return optionList(createDefaultLayout(), sortOrder, this.parameterLabelRenderer);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String optionList(Layout layout, Comparator<Field> optionSort, IParamLabelRenderer valueLabelRenderer) {
/* 3167 */       List<Field> fields = new ArrayList<>(this.optionFields);
/* 3168 */       if (optionSort != null) {
/* 3169 */         Collections.sort(fields, optionSort);
/*      */       }
/* 3171 */       layout.addOptions(fields, valueLabelRenderer);
/* 3172 */       return layout.toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String parameterList() {
/* 3180 */       return parameterList(createDefaultLayout(), this.parameterLabelRenderer);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String parameterList(Layout layout, IParamLabelRenderer paramLabelRenderer) {
/* 3189 */       layout.addPositionalParameters(this.positionalParametersFields, paramLabelRenderer);
/* 3190 */       return layout.toString();
/*      */     }
/*      */     
/*      */     private static String heading(Ansi ansi, String values, Object... params) {
/* 3194 */       StringBuilder sb = join(ansi, new String[] { values }, new StringBuilder(), params);
/* 3195 */       String result = sb.toString();
/*      */       
/* 3197 */       result = result.endsWith(System.getProperty("line.separator")) ? result.substring(0, result.length() - System.getProperty("line.separator").length()) : result;
/* 3198 */       return result + new String(spaces(countTrailingSpaces(values)));
/*      */     } private static char[] spaces(int length) {
/* 3200 */       char[] result = new char[length]; Arrays.fill(result, ' '); return result;
/*      */     } private static int countTrailingSpaces(String str) {
/* 3202 */       if (str == null) return 0; 
/* 3203 */       int trailingSpaces = 0;
/* 3204 */       for (int i = str.length() - 1; i >= 0 && str.charAt(i) == ' '; ) { trailingSpaces++; i--; }
/* 3205 */        return trailingSpaces;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static StringBuilder join(Ansi ansi, String[] values, StringBuilder sb, Object... params) {
/* 3215 */       if (values != null) {
/* 3216 */         TextTable table = new TextTable(ansi, new int[] { 80 });
/* 3217 */         table.indentWrappedLines = 0;
/* 3218 */         for (String summaryLine : values) {
/* 3219 */           ansi.getClass(); Ansi.Text[] lines = (new Ansi.Text(format(summaryLine, params))).splitLines();
/* 3220 */           for (Ansi.Text line : lines) { table.addRowValues(new Ansi.Text[] { line }); }
/*      */         
/* 3222 */         }  table.toString(sb);
/*      */       } 
/* 3224 */       return sb;
/*      */     }
/*      */     private static String format(String formatString, Object... params) {
/* 3227 */       return (formatString == null) ? "" : String.format(formatString, params);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String customSynopsis(Object... params) {
/* 3236 */       return join(ansi(), this.customSynopsis, new StringBuilder(), params).toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String description(Object... params) {
/* 3245 */       return join(ansi(), this.description, new StringBuilder(), params).toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String header(Object... params) {
/* 3254 */       return join(ansi(), this.header, new StringBuilder(), params).toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String footer(Object... params) {
/* 3263 */       return join(ansi(), this.footer, new StringBuilder(), params).toString();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String headerHeading(Object... params) {
/* 3270 */       return heading(ansi(), this.headerHeading, params);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String synopsisHeading(Object... params) {
/* 3277 */       return heading(ansi(), this.synopsisHeading, params);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String descriptionHeading(Object... params) {
/* 3285 */       return CommandLine.empty(this.descriptionHeading) ? "" : heading(ansi(), this.descriptionHeading, params);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String parameterListHeading(Object... params) {
/* 3293 */       return this.positionalParametersFields.isEmpty() ? "" : heading(ansi(), this.parameterListHeading, params);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String optionListHeading(Object... params) {
/* 3301 */       return this.optionFields.isEmpty() ? "" : heading(ansi(), this.optionListHeading, params);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String commandListHeading(Object... params) {
/* 3309 */       return this.commands.isEmpty() ? "" : heading(ansi(), this.commandListHeading, params);
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public String footerHeading(Object... params) {
/* 3316 */       return heading(ansi(), this.footerHeading, params);
/*      */     }
/*      */ 
/*      */     
/*      */     public String commandList() {
/* 3321 */       if (this.commands.isEmpty()) return ""; 
/* 3322 */       int commandLength = maxLength(this.commands.keySet());
/* 3323 */       TextTable textTable = new TextTable(ansi(), new Column[] { new Column(commandLength + 2, 2, Column.Overflow.SPAN), new Column(80 - commandLength + 2, 2, Column.Overflow.WRAP) });
/*      */ 
/*      */ 
/*      */       
/* 3327 */       for (Map.Entry<String, Help> entry : this.commands.entrySet()) {
/* 3328 */         Help command = entry.getValue();
/* 3329 */         String header = (command.header != null && command.header.length > 0) ? command.header[0] : ((command.description != null && command.description.length > 0) ? command.description[0] : "");
/*      */         
/* 3331 */         (new Ansi.Text[2])[0] = this.colorScheme.commandText(entry.getKey()); ansi().getClass(); textTable.addRowValues(new Ansi.Text[] { null, new Ansi.Text(header) });
/*      */       } 
/* 3333 */       return textTable.toString();
/*      */     }
/*      */     private static int maxLength(Collection<String> any) {
/* 3336 */       List<String> strings = new ArrayList<>(any);
/* 3337 */       Collections.sort(strings, Collections.reverseOrder(shortestFirst()));
/* 3338 */       return ((String)strings.get(0)).length();
/*      */     }
/*      */     private static String join(String[] names, int offset, int length, String separator) {
/* 3341 */       if (names == null) return ""; 
/* 3342 */       StringBuilder result = new StringBuilder();
/* 3343 */       for (int i = offset; i < offset + length; i++) {
/* 3344 */         result.append((i > offset) ? separator : "").append(names[i]);
/*      */       }
/* 3346 */       return result.toString();
/*      */     }
/*      */     private static String stringOf(char chr, int length) {
/* 3349 */       char[] buff = new char[length];
/* 3350 */       Arrays.fill(buff, chr);
/* 3351 */       return new String(buff);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public Layout createDefaultLayout() {
/* 3357 */       return new Layout(this.colorScheme, new TextTable(this.colorScheme.ansi()), createDefaultOptionRenderer(), createDefaultParameterRenderer());
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public IOptionRenderer createDefaultOptionRenderer() {
/* 3374 */       DefaultOptionRenderer result = new DefaultOptionRenderer();
/* 3375 */       result.requiredMarker = String.valueOf(this.requiredOptionMarker);
/* 3376 */       if (this.showDefaultValues != null && this.showDefaultValues.booleanValue()) {
/* 3377 */         result.command = this.command;
/*      */       }
/* 3379 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public static IOptionRenderer createMinimalOptionRenderer() {
/* 3385 */       return new MinimalOptionRenderer();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public IParameterRenderer createDefaultParameterRenderer() {
/* 3402 */       DefaultParameterRenderer result = new DefaultParameterRenderer();
/* 3403 */       result.requiredMarker = String.valueOf(this.requiredOptionMarker);
/* 3404 */       return result;
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public static IParameterRenderer createMinimalParameterRenderer() {
/* 3410 */       return new MinimalParameterRenderer();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public static IParamLabelRenderer createMinimalParamLabelRenderer() {
/* 3416 */       return new IParamLabelRenderer()
/*      */         {
/*      */           public CommandLine.Help.Ansi.Text renderParameterLabel(Field field, CommandLine.Help.Ansi ansi, List<CommandLine.Help.Ansi.IStyle> styles) {
/* 3419 */             String text = CommandLine.Help.DefaultParamLabelRenderer.renderParameterName(field);
/* 3420 */             return ansi.apply(text, styles);
/*      */           }
/*      */           public String separator() {
/* 3423 */             return "";
/*      */           }
/*      */         };
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public IParamLabelRenderer createDefaultParamLabelRenderer() {
/* 3432 */       return new DefaultParamLabelRenderer(this.separator);
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public static Comparator<Field> createShortOptionNameComparator() {
/* 3438 */       return new SortByShortestOptionNameAlphabetically();
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public static Comparator<Field> createShortOptionArityAndNameComparator() {
/* 3444 */       return new SortByOptionArityAndNameAlphabetically();
/*      */     }
/*      */ 
/*      */     
/*      */     public static Comparator<String> shortestFirst() {
/* 3449 */       return new ShortestFirst();
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public Ansi ansi() {
/* 3456 */       return this.colorScheme.ansi;
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static class DefaultOptionRenderer
/*      */       implements IOptionRenderer
/*      */     {
/* 3487 */       public String requiredMarker = " ";
/*      */       public Object command;
/*      */       private String sep;
/*      */       private boolean showDefault;
/*      */       
/*      */       public CommandLine.Help.Ansi.Text[][] render(CommandLine.Option option, Field field, CommandLine.Help.IParamLabelRenderer paramLabelRenderer, CommandLine.Help.ColorScheme scheme) {
/* 3493 */         String[] names = CommandLine.Help.ShortestFirst.sort(option.names());
/* 3494 */         int shortOptionCount = (names[0].length() == 2) ? 1 : 0;
/* 3495 */         String shortOption = (shortOptionCount > 0) ? names[0] : "";
/* 3496 */         this.sep = (shortOptionCount > 0 && names.length > 1) ? "," : "";
/*      */         
/* 3498 */         String longOption = CommandLine.Help.join(names, shortOptionCount, names.length - shortOptionCount, ", ");
/* 3499 */         CommandLine.Help.Ansi.Text longOptionText = createLongOptionText(field, paramLabelRenderer, scheme, longOption);
/*      */         
/* 3501 */         this.showDefault = (this.command != null && !option.help() && !CommandLine.isBoolean(field.getType()));
/* 3502 */         Object defaultValue = createDefaultValue(field);
/*      */         
/* 3504 */         String requiredOption = option.required() ? this.requiredMarker : "";
/* 3505 */         return renderDescriptionLines(option, scheme, requiredOption, shortOption, longOptionText, defaultValue);
/*      */       }
/*      */       
/*      */       private Object createDefaultValue(Field field) {
/* 3509 */         Object defaultValue = null;
/*      */         try {
/* 3511 */           defaultValue = field.get(this.command);
/* 3512 */           if (defaultValue == null) { this.showDefault = false; }
/* 3513 */           else if (field.getType().isArray())
/* 3514 */           { StringBuilder sb = new StringBuilder();
/* 3515 */             for (int i = 0; i < Array.getLength(defaultValue); i++) {
/* 3516 */               sb.append((i > 0) ? ", " : "").append(Array.get(defaultValue, i));
/*      */             }
/* 3518 */             defaultValue = sb.insert(0, "[").append("]").toString(); }
/*      */         
/* 3520 */         } catch (Exception ex) {
/* 3521 */           this.showDefault = false;
/*      */         } 
/* 3523 */         return defaultValue;
/*      */       }
/*      */       
/*      */       private CommandLine.Help.Ansi.Text createLongOptionText(Field field, CommandLine.Help.IParamLabelRenderer renderer, CommandLine.Help.ColorScheme scheme, String longOption) {
/* 3527 */         CommandLine.Help.Ansi.Text paramLabelText = renderer.renderParameterLabel(field, scheme.ansi(), scheme.optionParamStyles);
/*      */ 
/*      */         
/* 3530 */         if (paramLabelText.length > 0 && longOption.length() == 0) {
/* 3531 */           this.sep = renderer.separator();
/*      */           
/* 3533 */           int sepStart = paramLabelText.plainString().indexOf(this.sep);
/* 3534 */           CommandLine.Help.Ansi.Text prefix = paramLabelText.substring(0, sepStart);
/* 3535 */           paramLabelText = prefix.append(paramLabelText.substring(sepStart + this.sep.length()));
/*      */         } 
/* 3537 */         CommandLine.Help.Ansi.Text longOptionText = scheme.optionText(longOption);
/* 3538 */         longOptionText = longOptionText.append(paramLabelText);
/* 3539 */         return longOptionText;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       private CommandLine.Help.Ansi.Text[][] renderDescriptionLines(CommandLine.Option option, CommandLine.Help.ColorScheme scheme, String requiredOption, String shortOption, CommandLine.Help.Ansi.Text longOptionText, Object defaultValue) {
/* 3548 */         CommandLine.Help.Ansi.Text EMPTY = CommandLine.Help.Ansi.EMPTY_TEXT;
/* 3549 */         List<CommandLine.Help.Ansi.Text[]> result = (List)new ArrayList<>();
/* 3550 */         scheme.ansi().getClass(); CommandLine.Help.Ansi.Text[] descriptionFirstLines = (new CommandLine.Help.Ansi.Text(CommandLine.str(option.description(), 0))).splitLines();
/* 3551 */         if (descriptionFirstLines.length == 0) {
/* 3552 */           if (this.showDefault) {
/* 3553 */             scheme.ansi().getClass(); descriptionFirstLines = new CommandLine.Help.Ansi.Text[] { new CommandLine.Help.Ansi.Text("  Default: " + defaultValue) };
/* 3554 */             this.showDefault = false;
/*      */           } else {
/* 3556 */             descriptionFirstLines = new CommandLine.Help.Ansi.Text[] { EMPTY };
/*      */           } 
/*      */         }
/* 3559 */         (new CommandLine.Help.Ansi.Text[5])[0] = scheme.optionText(requiredOption); (new CommandLine.Help.Ansi.Text[5])[1] = scheme.optionText(shortOption); scheme
/* 3560 */           .ansi().getClass(); result.add(new CommandLine.Help.Ansi.Text[] { null, null, new CommandLine.Help.Ansi.Text(this.sep), longOptionText, descriptionFirstLines[0] }); int i;
/* 3561 */         for (i = 1; i < descriptionFirstLines.length; i++) {
/* 3562 */           result.add(new CommandLine.Help.Ansi.Text[] { EMPTY, EMPTY, EMPTY, EMPTY, descriptionFirstLines[i] });
/*      */         } 
/* 3564 */         for (i = 1; i < (option.description()).length; i++) {
/* 3565 */           scheme.ansi().getClass(); CommandLine.Help.Ansi.Text[] descriptionNextLines = (new CommandLine.Help.Ansi.Text(option.description()[i])).splitLines();
/* 3566 */           for (CommandLine.Help.Ansi.Text line : descriptionNextLines) {
/* 3567 */             result.add(new CommandLine.Help.Ansi.Text[] { EMPTY, EMPTY, EMPTY, EMPTY, line });
/*      */           } 
/*      */         } 
/* 3570 */         if (this.showDefault) {
/* 3571 */           (new CommandLine.Help.Ansi.Text[5])[0] = EMPTY; (new CommandLine.Help.Ansi.Text[5])[1] = EMPTY; (new CommandLine.Help.Ansi.Text[5])[2] = EMPTY; (new CommandLine.Help.Ansi.Text[5])[3] = EMPTY; scheme.ansi().getClass(); result.add(new CommandLine.Help.Ansi.Text[] { null, null, null, null, new CommandLine.Help.Ansi.Text("  Default: " + defaultValue) });
/*      */         } 
/* 3573 */         return result.<CommandLine.Help.Ansi.Text[]>toArray(new CommandLine.Help.Ansi.Text[result.size()][]);
/*      */       }
/*      */     }
/*      */     
/*      */     static class MinimalOptionRenderer
/*      */       implements IOptionRenderer
/*      */     {
/*      */       public CommandLine.Help.Ansi.Text[][] render(CommandLine.Option option, Field field, CommandLine.Help.IParamLabelRenderer parameterLabelRenderer, CommandLine.Help.ColorScheme scheme) {
/* 3581 */         CommandLine.Help.Ansi.Text optionText = scheme.optionText(option.names()[0]);
/* 3582 */         CommandLine.Help.Ansi.Text paramLabelText = parameterLabelRenderer.renderParameterLabel(field, scheme.ansi(), scheme.optionParamStyles);
/* 3583 */         optionText = optionText.append(paramLabelText);
/* 3584 */         (new CommandLine.Help.Ansi.Text[2])[0] = optionText; scheme
/* 3585 */           .ansi().getClass(); return new CommandLine.Help.Ansi.Text[][] { { null, new CommandLine.Help.Ansi.Text(((option.description()).length == 0) ? "" : option.description()[0]) } };
/*      */       }
/*      */     }
/*      */     
/*      */     static class MinimalParameterRenderer
/*      */       implements IParameterRenderer
/*      */     {
/*      */       public CommandLine.Help.Ansi.Text[][] render(CommandLine.Parameters param, Field field, CommandLine.Help.IParamLabelRenderer parameterLabelRenderer, CommandLine.Help.ColorScheme scheme) {
/* 3593 */         (new CommandLine.Help.Ansi.Text[2])[0] = parameterLabelRenderer.renderParameterLabel(field, scheme.ansi(), scheme.parameterStyles); scheme
/* 3594 */           .ansi().getClass(); return new CommandLine.Help.Ansi.Text[][] { { null, new CommandLine.Help.Ansi.Text(((param.description()).length == 0) ? "" : param.description()[0]) } };
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static class DefaultParameterRenderer
/*      */       implements IParameterRenderer
/*      */     {
/* 3625 */       public String requiredMarker = " ";
/*      */       
/*      */       public CommandLine.Help.Ansi.Text[][] render(CommandLine.Parameters params, Field field, CommandLine.Help.IParamLabelRenderer paramLabelRenderer, CommandLine.Help.ColorScheme scheme) {
/* 3628 */         CommandLine.Help.Ansi.Text label = paramLabelRenderer.renderParameterLabel(field, scheme.ansi(), scheme.parameterStyles);
/* 3629 */         CommandLine.Help.Ansi.Text requiredParameter = scheme.parameterText(((CommandLine.Range.parameterArity(field)).min > 0) ? this.requiredMarker : "");
/*      */         
/* 3631 */         CommandLine.Help.Ansi.Text EMPTY = CommandLine.Help.Ansi.EMPTY_TEXT;
/* 3632 */         List<CommandLine.Help.Ansi.Text[]> result = (List)new ArrayList<>();
/* 3633 */         scheme.ansi().getClass(); CommandLine.Help.Ansi.Text[] descriptionFirstLines = (new CommandLine.Help.Ansi.Text(CommandLine.str(params.description(), 0))).splitLines();
/* 3634 */         if (descriptionFirstLines.length == 0) descriptionFirstLines = new CommandLine.Help.Ansi.Text[] { EMPTY }; 
/* 3635 */         result.add(new CommandLine.Help.Ansi.Text[] { requiredParameter, EMPTY, EMPTY, label, descriptionFirstLines[0] }); int i;
/* 3636 */         for (i = 1; i < descriptionFirstLines.length; i++) {
/* 3637 */           result.add(new CommandLine.Help.Ansi.Text[] { EMPTY, EMPTY, EMPTY, EMPTY, descriptionFirstLines[i] });
/*      */         } 
/* 3639 */         for (i = 1; i < (params.description()).length; i++) {
/* 3640 */           scheme.ansi().getClass(); CommandLine.Help.Ansi.Text[] descriptionNextLines = (new CommandLine.Help.Ansi.Text(params.description()[i])).splitLines();
/* 3641 */           for (CommandLine.Help.Ansi.Text line : descriptionNextLines) {
/* 3642 */             result.add(new CommandLine.Help.Ansi.Text[] { EMPTY, EMPTY, EMPTY, EMPTY, line });
/*      */           } 
/*      */         } 
/* 3645 */         return result.<CommandLine.Help.Ansi.Text[]>toArray(new CommandLine.Help.Ansi.Text[result.size()][]);
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     static class DefaultParamLabelRenderer
/*      */       implements IParamLabelRenderer
/*      */     {
/*      */       public final String separator;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public DefaultParamLabelRenderer(String separator) {
/* 3675 */         this.separator = CommandLine.Assert.<String>notNull(separator, "separator");
/*      */       }
/*      */       public String separator() {
/* 3678 */         return this.separator;
/*      */       }
/*      */       public CommandLine.Help.Ansi.Text renderParameterLabel(Field field, CommandLine.Help.Ansi ansi, List<CommandLine.Help.Ansi.IStyle> styles) {
/* 3681 */         boolean isOptionParameter = field.isAnnotationPresent((Class)CommandLine.Option.class);
/* 3682 */         CommandLine.Range arity = isOptionParameter ? CommandLine.Range.optionArity(field) : CommandLine.Range.parameterCapacity(field);
/* 3683 */         String split = isOptionParameter ? ((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).split() : ((CommandLine.Parameters)field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).split();
/* 3684 */         ansi.getClass(); CommandLine.Help.Ansi.Text result = new CommandLine.Help.Ansi.Text("");
/* 3685 */         String sep = isOptionParameter ? this.separator : "";
/* 3686 */         CommandLine.Help.Ansi.Text paramName = ansi.apply(renderParameterName(field), styles);
/* 3687 */         if (!CommandLine.empty(split)) paramName = paramName.append("[" + split).append(paramName).append("]...");  int i;
/* 3688 */         for (i = 0; i < arity.min; i++) {
/* 3689 */           result = result.append(sep).append(paramName);
/* 3690 */           sep = " ";
/*      */         } 
/* 3692 */         if (arity.isVariable) {
/* 3693 */           if (result.length == 0) {
/* 3694 */             result = result.append(sep + "[").append(paramName).append("]...");
/* 3695 */           } else if (!result.plainString().endsWith("...")) {
/* 3696 */             result = result.append("...");
/*      */           } 
/*      */         } else {
/* 3699 */           sep = (result.length == 0) ? (isOptionParameter ? this.separator : "") : " ";
/* 3700 */           for (i = arity.min; i < arity.max; i++) {
/* 3701 */             if (sep.trim().length() == 0) {
/* 3702 */               result = result.append(sep + "[").append(paramName);
/*      */             } else {
/* 3704 */               result = result.append("[" + sep).append(paramName);
/*      */             } 
/* 3706 */             sep = " ";
/*      */           } 
/* 3708 */           for (i = arity.min; i < arity.max; ) { result = result.append("]"); i++; }
/*      */         
/* 3710 */         }  return result;
/*      */       }
/*      */       private static String renderParameterName(Field field) {
/* 3713 */         String result = null;
/* 3714 */         if (field.isAnnotationPresent((Class)CommandLine.Option.class)) {
/* 3715 */           result = ((CommandLine.Option)field.<CommandLine.Option>getAnnotation(CommandLine.Option.class)).paramLabel();
/* 3716 */         } else if (field.isAnnotationPresent((Class)CommandLine.Parameters.class)) {
/* 3717 */           result = ((CommandLine.Parameters)field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).paramLabel();
/*      */         } 
/* 3719 */         if (result != null && result.trim().length() > 0) {
/* 3720 */           return result.trim();
/*      */         }
/* 3722 */         String name = field.getName();
/* 3723 */         if (Map.class.isAssignableFrom(field.getType())) {
/* 3724 */           Class<?>[] paramTypes = CommandLine.getTypeAttribute(field);
/* 3725 */           if (paramTypes.length < 2 || paramTypes[0] == null || paramTypes[1] == null)
/* 3726 */           { name = "String=String"; }
/* 3727 */           else { name = paramTypes[0].getSimpleName() + "=" + paramTypes[1].getSimpleName(); }
/*      */         
/* 3729 */         }  return "<" + name + ">";
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public static class Layout
/*      */     {
/*      */       protected final CommandLine.Help.ColorScheme colorScheme;
/*      */ 
/*      */       
/*      */       protected final CommandLine.Help.TextTable table;
/*      */ 
/*      */       
/*      */       protected CommandLine.Help.IOptionRenderer optionRenderer;
/*      */ 
/*      */       
/*      */       protected CommandLine.Help.IParameterRenderer parameterRenderer;
/*      */ 
/*      */ 
/*      */       
/*      */       public Layout(CommandLine.Help.ColorScheme colorScheme) {
/* 3751 */         this(colorScheme, new CommandLine.Help.TextTable(colorScheme.ansi()));
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public Layout(CommandLine.Help.ColorScheme colorScheme, CommandLine.Help.TextTable textTable) {
/* 3759 */         this(colorScheme, textTable, new CommandLine.Help.DefaultOptionRenderer(), new CommandLine.Help.DefaultParameterRenderer());
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public Layout(CommandLine.Help.ColorScheme colorScheme, CommandLine.Help.TextTable textTable, CommandLine.Help.IOptionRenderer optionRenderer, CommandLine.Help.IParameterRenderer parameterRenderer) {
/* 3768 */         this.colorScheme = CommandLine.Assert.<CommandLine.Help.ColorScheme>notNull(colorScheme, "colorScheme");
/* 3769 */         this.table = CommandLine.Assert.<CommandLine.Help.TextTable>notNull(textTable, "textTable");
/* 3770 */         this.optionRenderer = CommandLine.Assert.<CommandLine.Help.IOptionRenderer>notNull(optionRenderer, "optionRenderer");
/* 3771 */         this.parameterRenderer = CommandLine.Assert.<CommandLine.Help.IParameterRenderer>notNull(parameterRenderer, "parameterRenderer");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void layout(Field field, CommandLine.Help.Ansi.Text[][] cellValues) {
/* 3781 */         for (CommandLine.Help.Ansi.Text[] oneRow : cellValues) {
/* 3782 */           this.table.addRowValues(oneRow);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void addOptions(List<Field> fields, CommandLine.Help.IParamLabelRenderer paramLabelRenderer) {
/* 3789 */         for (Field field : fields) {
/* 3790 */           CommandLine.Option option = field.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
/* 3791 */           if (!option.hidden()) {
/* 3792 */             addOption(field, paramLabelRenderer);
/*      */           }
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void addOption(Field field, CommandLine.Help.IParamLabelRenderer paramLabelRenderer) {
/* 3804 */         CommandLine.Option option = field.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
/* 3805 */         CommandLine.Help.Ansi.Text[][] values = this.optionRenderer.render(option, field, paramLabelRenderer, this.colorScheme);
/* 3806 */         layout(field, values);
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void addPositionalParameters(List<Field> fields, CommandLine.Help.IParamLabelRenderer paramLabelRenderer) {
/* 3812 */         for (Field field : fields) {
/* 3813 */           CommandLine.Parameters parameters = field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class);
/* 3814 */           if (!parameters.hidden()) {
/* 3815 */             addPositionalParameter(field, paramLabelRenderer);
/*      */           }
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void addPositionalParameter(Field field, CommandLine.Help.IParamLabelRenderer paramLabelRenderer) {
/* 3827 */         CommandLine.Parameters option = field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class);
/* 3828 */         CommandLine.Help.Ansi.Text[][] values = this.parameterRenderer.render(option, field, paramLabelRenderer, this.colorScheme);
/* 3829 */         layout(field, values);
/*      */       }
/*      */       
/*      */       public String toString() {
/* 3833 */         return this.table.toString();
/*      */       }
/*      */     }
/*      */     
/*      */     static class ShortestFirst
/*      */       implements Comparator<String> {
/*      */       public int compare(String o1, String o2) {
/* 3840 */         return o1.length() - o2.length();
/*      */       }
/*      */       
/*      */       public static String[] sort(String[] names) {
/* 3844 */         Arrays.sort(names, new ShortestFirst());
/* 3845 */         return names;
/*      */       }
/*      */     }
/*      */     
/*      */     static class SortByShortestOptionNameAlphabetically
/*      */       implements Comparator<Field>
/*      */     {
/*      */       public int compare(Field f1, Field f2) {
/* 3853 */         CommandLine.Option o1 = f1.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
/* 3854 */         CommandLine.Option o2 = f2.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
/* 3855 */         if (o1 == null) return 1;  if (o2 == null) return -1; 
/* 3856 */         String[] names1 = CommandLine.Help.ShortestFirst.sort(o1.names());
/* 3857 */         String[] names2 = CommandLine.Help.ShortestFirst.sort(o2.names());
/* 3858 */         int result = names1[0].toUpperCase().compareTo(names2[0].toUpperCase());
/* 3859 */         result = (result == 0) ? -names1[0].compareTo(names2[0]) : result;
/* 3860 */         return (o1.help() == o2.help()) ? result : (o2.help() ? -1 : 1);
/*      */       }
/*      */     }
/*      */     
/*      */     static class SortByOptionArityAndNameAlphabetically
/*      */       extends SortByShortestOptionNameAlphabetically {
/*      */       public int compare(Field f1, Field f2) {
/* 3867 */         CommandLine.Option o1 = f1.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
/* 3868 */         CommandLine.Option o2 = f2.<CommandLine.Option>getAnnotation(CommandLine.Option.class);
/* 3869 */         CommandLine.Range arity1 = CommandLine.Range.optionArity(f1);
/* 3870 */         CommandLine.Range arity2 = CommandLine.Range.optionArity(f2);
/* 3871 */         int result = arity1.max - arity2.max;
/* 3872 */         if (result == 0) {
/* 3873 */           result = arity1.min - arity2.min;
/*      */         }
/* 3875 */         if (result == 0) {
/* 3876 */           if (CommandLine.isMultiValue(f1) && !CommandLine.isMultiValue(f2)) result = 1; 
/* 3877 */           if (!CommandLine.isMultiValue(f1) && CommandLine.isMultiValue(f2)) result = -1; 
/*      */         } 
/* 3879 */         return (result == 0) ? super.compare(f1, f2) : result;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public static class TextTable
/*      */     {
/*      */       public final CommandLine.Help.Column[] columns;
/*      */ 
/*      */ 
/*      */       
/*      */       public static class Cell
/*      */       {
/*      */         public final int column;
/*      */ 
/*      */         
/*      */         public final int row;
/*      */ 
/*      */         
/*      */         public Cell(int column, int row) {
/* 3900 */           this.column = column; this.row = row;
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/* 3907 */       protected final List<CommandLine.Help.Ansi.Text> columnValues = new ArrayList<>();
/*      */ 
/*      */       
/* 3910 */       public int indentWrappedLines = 2;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       private final CommandLine.Help.Ansi ansi;
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public TextTable(CommandLine.Help.Ansi ansi) {
/* 3926 */         this(ansi, new CommandLine.Help.Column[] { new CommandLine.Help.Column(2, 0, CommandLine.Help.Column.Overflow.TRUNCATE), new CommandLine.Help.Column(2, 0, CommandLine.Help.Column.Overflow.TRUNCATE), new CommandLine.Help.Column(1, 0, CommandLine.Help.Column.Overflow.TRUNCATE), new CommandLine.Help.Column(24, 1, CommandLine.Help.Column.Overflow.SPAN), new CommandLine.Help.Column(51, 1, CommandLine.Help.Column.Overflow.WRAP) });
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public TextTable(CommandLine.Help.Ansi ansi, int... columnWidths) {
/* 3941 */         this.ansi = CommandLine.Assert.<CommandLine.Help.Ansi>notNull(ansi, "ansi");
/* 3942 */         this.columns = new CommandLine.Help.Column[columnWidths.length];
/* 3943 */         for (int i = 0; i < columnWidths.length; i++) {
/* 3944 */           this.columns[i] = new CommandLine.Help.Column(columnWidths[i], 0, (i == columnWidths.length - 1) ? CommandLine.Help.Column.Overflow.SPAN : CommandLine.Help.Column.Overflow.WRAP);
/*      */         }
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public TextTable(CommandLine.Help.Ansi ansi, CommandLine.Help.Column... columns) {
/* 3951 */         this.ansi = CommandLine.Assert.<CommandLine.Help.Ansi>notNull(ansi, "ansi");
/* 3952 */         this.columns = CommandLine.Assert.<CommandLine.Help.Column[]>notNull(columns, "columns");
/* 3953 */         if (columns.length == 0) throw new IllegalArgumentException("At least one column is required");
/*      */       
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public CommandLine.Help.Ansi.Text textAt(int row, int col) {
/* 3960 */         return this.columnValues.get(col + row * this.columns.length);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       @Deprecated
/*      */       public CommandLine.Help.Ansi.Text cellAt(int row, int col) {
/* 3968 */         return textAt(row, col);
/*      */       }
/*      */       
/*      */       public int rowCount() {
/* 3972 */         return this.columnValues.size() / this.columns.length;
/*      */       }
/*      */       
/*      */       public void addEmptyRow() {
/* 3976 */         for (int i = 0; i < this.columns.length; i++) {
/* 3977 */           this.ansi.getClass(); this.columnValues.add(new CommandLine.Help.Ansi.Text((this.columns[i]).width));
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public void addRowValues(String... values) {
/* 3984 */         CommandLine.Help.Ansi.Text[] array = new CommandLine.Help.Ansi.Text[values.length];
/* 3985 */         for (int i = 0; i < array.length; i++) {
/* 3986 */           this.ansi.getClass(); array[i] = (values[i] == null) ? CommandLine.Help.Ansi.EMPTY_TEXT : new CommandLine.Help.Ansi.Text(values[i]);
/*      */         } 
/* 3988 */         addRowValues(array);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public void addRowValues(CommandLine.Help.Ansi.Text... values) {
/* 3998 */         if (values.length > this.columns.length) {
/* 3999 */           throw new IllegalArgumentException(values.length + " values don't fit in " + this.columns.length + " columns");
/*      */         }
/*      */         
/* 4002 */         addEmptyRow();
/* 4003 */         for (int col = 0; col < values.length; col++) {
/* 4004 */           int row = rowCount() - 1;
/* 4005 */           Cell cell = putValue(row, col, values[col]);
/*      */ 
/*      */           
/* 4008 */           if ((cell.row != row || cell.column != col) && col != values.length - 1) {
/* 4009 */             addEmptyRow();
/*      */           }
/*      */         } 
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public Cell putValue(int row, int col, CommandLine.Help.Ansi.Text value) {
/*      */         int startColumn;
/*      */         BreakIterator lineBreakIterator;
/* 4026 */         if (row > rowCount() - 1) {
/* 4027 */           throw new IllegalArgumentException("Cannot write to row " + row + ": rowCount=" + rowCount());
/*      */         }
/* 4029 */         if (value == null || value.plain.length() == 0) return new Cell(col, row); 
/* 4030 */         CommandLine.Help.Column column = this.columns[col];
/* 4031 */         int indent = column.indent;
/* 4032 */         switch (column.overflow) {
/*      */           case TRUNCATE:
/* 4034 */             copy(value, textAt(row, col), indent);
/* 4035 */             return new Cell(col, row);
/*      */           case SPAN:
/* 4037 */             startColumn = col;
/*      */             while (true)
/* 4039 */             { boolean lastColumn = (col == this.columns.length - 1);
/*      */ 
/*      */               
/* 4042 */               int charsWritten = lastColumn ? copy(BreakIterator.getLineInstance(), value, textAt(row, col), indent) : copy(value, textAt(row, col), indent);
/* 4043 */               value = value.substring(charsWritten);
/* 4044 */               indent = 0;
/* 4045 */               if (value.length > 0) {
/* 4046 */                 col++;
/*      */               }
/* 4048 */               if (value.length > 0 && col >= this.columns.length) {
/* 4049 */                 addEmptyRow();
/* 4050 */                 row++;
/* 4051 */                 col = startColumn;
/* 4052 */                 indent = column.indent + this.indentWrappedLines;
/*      */               } 
/* 4054 */               if (value.length <= 0)
/* 4055 */                 return new Cell(col, row);  } 
/*      */           case WRAP:
/* 4057 */             lineBreakIterator = BreakIterator.getLineInstance();
/*      */             while (true) {
/* 4059 */               int charsWritten = copy(lineBreakIterator, value, textAt(row, col), indent);
/* 4060 */               value = value.substring(charsWritten);
/* 4061 */               indent = column.indent + this.indentWrappedLines;
/* 4062 */               if (value.length > 0) {
/* 4063 */                 row++;
/* 4064 */                 addEmptyRow();
/*      */               } 
/* 4066 */               if (value.length <= 0)
/* 4067 */                 return new Cell(col, row); 
/*      */             } 
/* 4069 */         }  throw new IllegalStateException(column.overflow.toString());
/*      */       }
/*      */       private static int length(CommandLine.Help.Ansi.Text str) {
/* 4072 */         return str.length;
/*      */       }
/*      */ 
/*      */       
/*      */       private int copy(BreakIterator line, CommandLine.Help.Ansi.Text text, CommandLine.Help.Ansi.Text columnValue, int offset) {
/* 4077 */         line.setText(text.plainString().replace("-", "ÿ"));
/* 4078 */         int done = 0; int end;
/* 4079 */         for (int start = line.first(); end != -1; ) {
/* 4080 */           CommandLine.Help.Ansi.Text word = text.substring(start, end);
/* 4081 */           if (columnValue.maxLength >= offset + done + length(word)) {
/* 4082 */             done += copy(word, columnValue, offset + done);
/*      */             start = end;
/*      */             end = line.next();
/*      */           } 
/*      */         } 
/* 4087 */         if (done == 0 && length(text) > columnValue.maxLength)
/*      */         {
/* 4089 */           done = copy(text, columnValue, offset);
/*      */         }
/* 4091 */         return done;
/*      */       }
/*      */       private static int copy(CommandLine.Help.Ansi.Text value, CommandLine.Help.Ansi.Text destination, int offset) {
/* 4094 */         int length = Math.min(value.length, destination.maxLength - offset);
/* 4095 */         value.getStyledChars(value.from, length, destination, offset);
/* 4096 */         return length;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public StringBuilder toString(StringBuilder text) {
/* 4103 */         int columnCount = this.columns.length;
/* 4104 */         StringBuilder row = new StringBuilder(80);
/* 4105 */         for (int i = 0; i < this.columnValues.size(); i++) {
/* 4106 */           CommandLine.Help.Ansi.Text column = this.columnValues.get(i);
/* 4107 */           row.append(column.toString());
/* 4108 */           row.append(new String(CommandLine.Help.spaces((this.columns[i % columnCount]).width - column.length)));
/* 4109 */           if (i % columnCount == columnCount - 1) {
/* 4110 */             int lastChar = row.length() - 1;
/* 4111 */             for (; lastChar >= 0 && row.charAt(lastChar) == ' '; lastChar--);
/* 4112 */             row.setLength(lastChar + 1);
/* 4113 */             text.append(row.toString()).append(System.getProperty("line.separator"));
/* 4114 */             row.setLength(0);
/*      */           } 
/*      */         } 
/*      */         
/* 4118 */         return text;
/*      */       }
/*      */       public String toString() {
/* 4121 */         return toString(new StringBuilder()).toString();
/*      */       } }
/*      */     
/*      */     public static class Column { public final int width;
/*      */       public final int indent;
/*      */       public final Overflow overflow;
/*      */       
/*      */       public enum Overflow {
/* 4129 */         TRUNCATE, SPAN, WRAP;
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public Column(int width, int indent, Overflow overflow) {
/* 4140 */         this.width = width;
/* 4141 */         this.indent = indent;
/* 4142 */         this.overflow = CommandLine.Assert.<Overflow>notNull(overflow, "overflow");
/*      */       } }
/*      */ 
/*      */ 
/*      */     
/*      */     public enum Overflow
/*      */     {
/*      */       TRUNCATE, SPAN, WRAP;
/*      */     }
/*      */     
/*      */     public static class ColorScheme
/*      */     {
/* 4154 */       public final List<CommandLine.Help.Ansi.IStyle> commandStyles = new ArrayList<>();
/* 4155 */       public final List<CommandLine.Help.Ansi.IStyle> optionStyles = new ArrayList<>();
/* 4156 */       public final List<CommandLine.Help.Ansi.IStyle> parameterStyles = new ArrayList<>();
/* 4157 */       public final List<CommandLine.Help.Ansi.IStyle> optionParamStyles = new ArrayList<>();
/*      */       private final CommandLine.Help.Ansi ansi;
/*      */       
/*      */       public ColorScheme() {
/* 4161 */         this(CommandLine.Help.Ansi.AUTO);
/*      */       }
/*      */ 
/*      */       
/*      */       public ColorScheme(CommandLine.Help.Ansi ansi) {
/* 4166 */         this.ansi = CommandLine.Assert.<CommandLine.Help.Ansi>notNull(ansi, "ansi");
/*      */       }
/*      */ 
/*      */       
/*      */       public ColorScheme commands(CommandLine.Help.Ansi.IStyle... styles) {
/* 4171 */         return addAll(this.commandStyles, styles);
/*      */       }
/*      */       
/*      */       public ColorScheme options(CommandLine.Help.Ansi.IStyle... styles) {
/* 4175 */         return addAll(this.optionStyles, styles);
/*      */       }
/*      */       
/*      */       public ColorScheme parameters(CommandLine.Help.Ansi.IStyle... styles) {
/* 4179 */         return addAll(this.parameterStyles, styles);
/*      */       }
/*      */       
/*      */       public ColorScheme optionParams(CommandLine.Help.Ansi.IStyle... styles) {
/* 4183 */         return addAll(this.optionParamStyles, styles);
/*      */       }
/*      */       
/*      */       public CommandLine.Help.Ansi.Text commandText(String command) {
/* 4187 */         return ansi().apply(command, this.commandStyles);
/*      */       }
/*      */       
/*      */       public CommandLine.Help.Ansi.Text optionText(String option) {
/* 4191 */         return ansi().apply(option, this.optionStyles);
/*      */       }
/*      */       
/*      */       public CommandLine.Help.Ansi.Text parameterText(String parameter) {
/* 4195 */         return ansi().apply(parameter, this.parameterStyles);
/*      */       }
/*      */       
/*      */       public CommandLine.Help.Ansi.Text optionParamText(String optionParam) {
/* 4199 */         return ansi().apply(optionParam, this.optionParamStyles);
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public ColorScheme applySystemProperties() {
/* 4211 */         replace(this.commandStyles, System.getProperty("picocli.color.commands"));
/* 4212 */         replace(this.optionStyles, System.getProperty("picocli.color.options"));
/* 4213 */         replace(this.parameterStyles, System.getProperty("picocli.color.parameters"));
/* 4214 */         replace(this.optionParamStyles, System.getProperty("picocli.color.optionParams"));
/* 4215 */         return this;
/*      */       }
/*      */       private void replace(List<CommandLine.Help.Ansi.IStyle> styles, String property) {
/* 4218 */         if (property != null) {
/* 4219 */           styles.clear();
/* 4220 */           addAll(styles, CommandLine.Help.Ansi.Style.parse(property));
/*      */         } 
/*      */       }
/*      */       private ColorScheme addAll(List<CommandLine.Help.Ansi.IStyle> styles, CommandLine.Help.Ansi.IStyle... add) {
/* 4224 */         styles.addAll(Arrays.asList(add));
/* 4225 */         return this;
/*      */       }
/*      */       
/*      */       public CommandLine.Help.Ansi ansi() {
/* 4229 */         return this.ansi;
/*      */       }
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public static ColorScheme defaultColorScheme(Ansi ansi) {
/* 4239 */       return (new ColorScheme(ansi))
/* 4240 */         .commands(new Ansi.IStyle[] { Ansi.Style.bold
/* 4241 */           }).options(new Ansi.IStyle[] { Ansi.Style.fg_yellow
/* 4242 */           }).parameters(new Ansi.IStyle[] { Ansi.Style.fg_yellow
/* 4243 */           }).optionParams(new Ansi.IStyle[] { Ansi.Style.italic });
/*      */     }
/*      */ 
/*      */ 
/*      */     
/*      */     public enum Ansi
/*      */     {
/* 4250 */       AUTO,
/*      */       
/* 4252 */       ON,
/*      */       
/* 4254 */       OFF;
/* 4255 */       static Text EMPTY_TEXT = new Text(0);
/* 4256 */       static final boolean isWindows = System.getProperty("os.name").startsWith("Windows");
/* 4257 */       static final boolean isXterm = (System.getenv("TERM") != null && System.getenv("TERM").startsWith("xterm"));
/* 4258 */       static final boolean ISATTY = calcTTY();
/*      */       static {
/*      */       
/*      */       }
/* 4262 */       static final boolean calcTTY() { if (isWindows && isXterm) return true;  
/* 4263 */         try { return (System.class.getDeclaredMethod("console", new Class[0]).invoke(null, new Object[0]) != null); }
/* 4264 */         catch (Throwable reflectionFailed) { return true; }
/*      */          } private static boolean ansiPossible() {
/* 4266 */         return (ISATTY && (!isWindows || isXterm));
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public boolean enabled() {
/* 4272 */         if (this == ON) return true; 
/* 4273 */         if (this == OFF) return false; 
/* 4274 */         return (System.getProperty("picocli.ansi") == null) ? ansiPossible() : Boolean.getBoolean("picocli.ansi");
/*      */       }
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public static interface IStyle
/*      */       {
/*      */         public static final String CSI = "\033[";
/*      */ 
/*      */ 
/*      */         
/*      */         String on();
/*      */ 
/*      */ 
/*      */         
/*      */         String off();
/*      */       }
/*      */ 
/*      */ 
/*      */       
/*      */       public enum Style
/*      */         implements IStyle
/*      */       {
/* 4298 */         reset(0, 0), bold(1, 21), faint(2, 22), italic(3, 23), underline(4, 24), blink(5, 25), reverse(7, 27),
/* 4299 */         fg_black(30, 39), fg_red(31, 39), fg_green(32, 39), fg_yellow(33, 39), fg_blue(34, 39), fg_magenta(35, 39), fg_cyan(36, 39), fg_white(37, 39),
/* 4300 */         bg_black(40, 49), bg_red(41, 49), bg_green(42, 49), bg_yellow(43, 49), bg_blue(44, 49), bg_magenta(45, 49), bg_cyan(46, 49), bg_white(47, 49);
/*      */         private final int startCode;
/*      */         private final int endCode;
/*      */         
/*      */         Style(int startCode, int endCode) {
/* 4305 */           this.startCode = startCode; this.endCode = endCode;
/*      */         } public String on() {
/* 4307 */           return "\033[" + this.startCode + "m";
/*      */         } public String off() {
/* 4309 */           return "\033[" + this.endCode + "m";
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public static String on(CommandLine.Help.Ansi.IStyle... styles) {
/* 4315 */           StringBuilder result = new StringBuilder();
/* 4316 */           for (CommandLine.Help.Ansi.IStyle style : styles) {
/* 4317 */             result.append(style.on());
/*      */           }
/* 4319 */           return result.toString();
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public static String off(CommandLine.Help.Ansi.IStyle... styles) {
/* 4325 */           StringBuilder result = new StringBuilder();
/* 4326 */           for (CommandLine.Help.Ansi.IStyle style : styles) {
/* 4327 */             result.append(style.off());
/*      */           }
/* 4329 */           return result.toString();
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public static CommandLine.Help.Ansi.IStyle fg(String str) {
/*      */           
/* 4339 */           try { return valueOf(str.toLowerCase(Locale.ENGLISH)); } catch (Exception exception) { 
/* 4340 */             try { return valueOf("fg_" + str.toLowerCase(Locale.ENGLISH)); } catch (Exception exception1)
/* 4341 */             { return new CommandLine.Help.Ansi.Palette256Color(true, str); }
/*      */              }
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public static CommandLine.Help.Ansi.IStyle bg(String str) {
/*      */           
/* 4351 */           try { return valueOf(str.toLowerCase(Locale.ENGLISH)); } catch (Exception exception) { 
/* 4352 */             try { return valueOf("bg_" + str.toLowerCase(Locale.ENGLISH)); } catch (Exception exception1)
/* 4353 */             { return new CommandLine.Help.Ansi.Palette256Color(false, str); }
/*      */              }
/*      */         
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public static CommandLine.Help.Ansi.IStyle[] parse(String commaSeparatedCodes) {
/* 4362 */           String[] codes = commaSeparatedCodes.split(",");
/* 4363 */           CommandLine.Help.Ansi.IStyle[] styles = new CommandLine.Help.Ansi.IStyle[codes.length];
/* 4364 */           for (int i = 0; i < codes.length; i++) {
/* 4365 */             if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("fg(")) {
/* 4366 */               int end = codes[i].indexOf(')');
/* 4367 */               styles[i] = fg(codes[i].substring(3, (end < 0) ? codes[i].length() : end));
/* 4368 */             } else if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("bg(")) {
/* 4369 */               int end = codes[i].indexOf(')');
/* 4370 */               styles[i] = bg(codes[i].substring(3, (end < 0) ? codes[i].length() : end));
/*      */             } else {
/* 4372 */               styles[i] = fg(codes[i]);
/*      */             } 
/*      */           } 
/* 4375 */           return styles;
/*      */         }
/*      */       }
/*      */       
/*      */       static class Palette256Color
/*      */         implements IStyle
/*      */       {
/*      */         private final int fgbg;
/*      */         private final int color;
/*      */         
/*      */         Palette256Color(boolean foreground, String color) {
/* 4386 */           this.fgbg = foreground ? 38 : 48;
/* 4387 */           String[] rgb = color.split(";");
/* 4388 */           if (rgb.length == 3) {
/* 4389 */             this.color = 16 + 36 * Integer.decode(rgb[0]).intValue() + 6 * Integer.decode(rgb[1]).intValue() + Integer.decode(rgb[2]).intValue();
/*      */           } else {
/* 4391 */             this.color = Integer.decode(color).intValue();
/*      */           } 
/*      */         }
/*      */         public String on() {
/* 4395 */           return String.format("\033[%d;5;%dm", new Object[] { Integer.valueOf(this.fgbg), Integer.valueOf(this.color) });
/*      */         } public String off() {
/* 4397 */           return "\033[" + (this.fgbg + 1) + "m";
/*      */         } }
/*      */       private static class StyledSection { int startIndex; int length; String startStyles;
/*      */         String endStyles;
/*      */         
/*      */         StyledSection(int start, int len, String style1, String style2) {
/* 4403 */           this.startIndex = start; this.length = len; this.startStyles = style1; this.endStyles = style2;
/*      */         }
/*      */         StyledSection withStartIndex(int newStart) {
/* 4406 */           return new StyledSection(newStart, this.length, this.startStyles, this.endStyles);
/*      */         } }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */       
/*      */       public Text apply(String plainText, List<IStyle> styles) {
/* 4418 */         if (plainText.length() == 0) return new Text(0); 
/* 4419 */         Text result = new Text(plainText.length());
/* 4420 */         IStyle[] all = styles.<IStyle>toArray(new IStyle[styles.size()]);
/* 4421 */         result.sections.add(new StyledSection(0, plainText
/* 4422 */               .length(), Style.on(all), Style.off(reverse(all)) + Style.reset.off()));
/* 4423 */         result.plain.append(plainText);
/* 4424 */         result.length = result.plain.length();
/* 4425 */         return result;
/*      */       }
/*      */       
/*      */       private static <T> T[] reverse(T[] all) {
/* 4429 */         for (int i = 0; i < all.length / 2; i++) {
/* 4430 */           T temp = all[i];
/* 4431 */           all[i] = all[all.length - i - 1];
/* 4432 */           all[all.length - i - 1] = temp;
/*      */         } 
/* 4434 */         return all;
/*      */       }
/*      */ 
/*      */       
/*      */       public class Text
/*      */         implements Cloneable
/*      */       {
/*      */         private final int maxLength;
/*      */         
/*      */         private int from;
/*      */         
/*      */         private int length;
/* 4446 */         private StringBuilder plain = new StringBuilder();
/* 4447 */         private List<CommandLine.Help.Ansi.StyledSection> sections = new ArrayList<>();
/*      */ 
/*      */         
/*      */         public Text(int maxLength) {
/* 4451 */           this.maxLength = maxLength;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public Text(String input) {
/* 4459 */           this.maxLength = -1;
/* 4460 */           this.plain.setLength(0);
/* 4461 */           int i = 0;
/*      */           
/*      */           while (true) {
/* 4464 */             int j = input.indexOf("@|", i);
/* 4465 */             if (j == -1) {
/* 4466 */               if (i == 0) {
/* 4467 */                 this.plain.append(input);
/* 4468 */                 this.length = this.plain.length();
/*      */                 return;
/*      */               } 
/* 4471 */               this.plain.append(input.substring(i, input.length()));
/* 4472 */               this.length = this.plain.length();
/*      */               return;
/*      */             } 
/* 4475 */             this.plain.append(input.substring(i, j));
/* 4476 */             int k = input.indexOf("|@", j);
/* 4477 */             if (k == -1) {
/* 4478 */               this.plain.append(input);
/* 4479 */               this.length = this.plain.length();
/*      */               
/*      */               return;
/*      */             } 
/* 4483 */             j += 2;
/* 4484 */             String spec = input.substring(j, k);
/* 4485 */             String[] items = spec.split(" ", 2);
/* 4486 */             if (items.length == 1) {
/* 4487 */               this.plain.append(input);
/* 4488 */               this.length = this.plain.length();
/*      */               
/*      */               return;
/*      */             } 
/* 4492 */             CommandLine.Help.Ansi.IStyle[] styles = CommandLine.Help.Ansi.Style.parse(items[0]);
/* 4493 */             addStyledSection(this.plain.length(), items[1].length(), 
/* 4494 */                 CommandLine.Help.Ansi.Style.on(styles), CommandLine.Help.Ansi.Style.off((CommandLine.Help.Ansi.IStyle[])CommandLine.Help.Ansi.reverse((T[])styles)) + CommandLine.Help.Ansi.Style.reset.off());
/* 4495 */             this.plain.append(items[1]);
/* 4496 */             i = k + 2;
/*      */           } 
/*      */         }
/*      */         private void addStyledSection(int start, int length, String startStyle, String endStyle) {
/* 4500 */           this.sections.add(new CommandLine.Help.Ansi.StyledSection(start, length, startStyle, endStyle));
/*      */         }
/*      */         public Object clone() {
/*      */           
/* 4504 */           try { return super.clone(); } catch (CloneNotSupportedException e) { throw new IllegalStateException(e); }
/*      */         
/*      */         }
/*      */         public Text[] splitLines() {
/* 4508 */           List<Text> result = new ArrayList<>();
/* 4509 */           boolean trailingEmptyString = false;
/* 4510 */           int start = 0, end = 0;
/* 4511 */           for (int i = 0; i < this.plain.length(); end = ++i) {
/* 4512 */             char c = this.plain.charAt(i);
/* 4513 */             boolean eol = (c == '\n');
/* 4514 */             int j = eol | ((c == '\r' && i + 1 < this.plain.length() && this.plain.charAt(i + 1) == '\n' && ++i > 0) ? 1 : 0);
/* 4515 */             j |= (c == '\r') ? 1 : 0;
/* 4516 */             if (j != 0) {
/* 4517 */               result.add(substring(start, end));
/* 4518 */               trailingEmptyString = (i == this.plain.length() - 1);
/* 4519 */               start = i + 1;
/*      */             } 
/*      */           } 
/* 4522 */           if (start < this.plain.length() || trailingEmptyString) {
/* 4523 */             result.add(substring(start, this.plain.length()));
/*      */           }
/* 4525 */           return result.<Text>toArray(new Text[result.size()]);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public Text substring(int start) {
/* 4532 */           return substring(start, this.length);
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public Text substring(int start, int end) {
/* 4540 */           Text result = (Text)clone();
/* 4541 */           this.from += start;
/* 4542 */           result.length = end - start;
/* 4543 */           return result;
/*      */         }
/*      */ 
/*      */ 
/*      */         
/*      */         public Text append(String string) {
/* 4549 */           return append(new Text(string));
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public Text append(Text other) {
/* 4556 */           Text result = (Text)clone();
/* 4557 */           result.plain = new StringBuilder(this.plain.toString().substring(this.from, this.from + this.length));
/* 4558 */           result.from = 0;
/* 4559 */           result.sections = new ArrayList<>();
/* 4560 */           for (CommandLine.Help.Ansi.StyledSection section : this.sections) {
/* 4561 */             result.sections.add(section.withStartIndex(section.startIndex - this.from));
/*      */           }
/* 4563 */           result.plain.append(other.plain.toString().substring(other.from, other.from + other.length));
/* 4564 */           for (CommandLine.Help.Ansi.StyledSection section : other.sections) {
/* 4565 */             int index = result.length + section.startIndex - other.from;
/* 4566 */             result.sections.add(section.withStartIndex(index));
/*      */           } 
/* 4568 */           result.length = result.plain.length();
/* 4569 */           return result;
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public void getStyledChars(int from, int length, Text destination, int offset) {
/* 4580 */           if (destination.length < offset) {
/* 4581 */             for (int i = destination.length; i < offset; i++) {
/* 4582 */               destination.plain.append(' ');
/*      */             }
/* 4584 */             destination.length = offset;
/*      */           } 
/* 4586 */           for (CommandLine.Help.Ansi.StyledSection section : this.sections) {
/* 4587 */             destination.sections.add(section.withStartIndex(section.startIndex - from + destination.length));
/*      */           }
/* 4589 */           destination.plain.append(this.plain.toString().substring(from, from + length));
/* 4590 */           destination.length = destination.plain.length();
/*      */         }
/*      */         
/*      */         public String plainString() {
/* 4594 */           return this.plain.toString().substring(this.from, this.from + this.length);
/*      */         }
/*      */         public boolean equals(Object obj) {
/* 4597 */           return toString().equals(String.valueOf(obj));
/*      */         } public int hashCode() {
/* 4599 */           return toString().hashCode();
/*      */         }
/*      */ 
/*      */ 
/*      */ 
/*      */         
/*      */         public String toString() {
/* 4606 */           if (!CommandLine.Help.Ansi.this.enabled()) {
/* 4607 */             return this.plain.toString().substring(this.from, this.from + this.length);
/*      */           }
/* 4609 */           if (this.length == 0) return ""; 
/* 4610 */           StringBuilder sb = new StringBuilder(this.plain.length() + 20 * this.sections.size());
/* 4611 */           CommandLine.Help.Ansi.StyledSection current = null;
/* 4612 */           int end = Math.min(this.from + this.length, this.plain.length());
/* 4613 */           for (int i = this.from; i < end; i++) {
/* 4614 */             CommandLine.Help.Ansi.StyledSection section = findSectionContaining(i);
/* 4615 */             if (section != current) {
/* 4616 */               if (current != null) sb.append(current.endStyles); 
/* 4617 */               if (section != null) sb.append(section.startStyles); 
/* 4618 */               current = section;
/*      */             } 
/* 4620 */             sb.append(this.plain.charAt(i));
/*      */           } 
/* 4622 */           if (current != null) sb.append(current.endStyles); 
/* 4623 */           return sb.toString();
/*      */         }
/*      */         
/*      */         private CommandLine.Help.Ansi.StyledSection findSectionContaining(int index) {
/* 4627 */           for (CommandLine.Help.Ansi.StyledSection section : this.sections) {
/* 4628 */             if (index >= section.startIndex && index < section.startIndex + section.length) {
/* 4629 */               return section;
/*      */             }
/*      */           } 
/* 4632 */           return null; } } } public enum Style implements Ansi.IStyle { reset(0, 0), bold(1, 21), faint(2, 22), italic(3, 23), underline(4, 24), blink(5, 25), reverse(7, 27), fg_black(30, 39), fg_red(31, 39), fg_green(32, 39), fg_yellow(33, 39), fg_blue(34, 39), fg_magenta(35, 39), fg_cyan(36, 39), fg_white(37, 39), bg_black(40, 49), bg_red(41, 49), bg_green(42, 49), bg_yellow(43, 49), bg_blue(44, 49), bg_magenta(45, 49), bg_cyan(46, 49), bg_white(47, 49); private final int startCode; private final int endCode; Style(int startCode, int endCode) { this.startCode = startCode; this.endCode = endCode; } public String on() { return "\033[" + this.startCode + "m"; } public String off() { return "\033[" + this.endCode + "m"; } public static String on(CommandLine.Help.Ansi.IStyle... styles) { StringBuilder result = new StringBuilder(); for (CommandLine.Help.Ansi.IStyle style : styles) result.append(style.on());  return result.toString(); } public static String off(CommandLine.Help.Ansi.IStyle... styles) { StringBuilder result = new StringBuilder(); for (CommandLine.Help.Ansi.IStyle style : styles) result.append(style.off());  return result.toString(); } public static CommandLine.Help.Ansi.IStyle fg(String str) { try { return valueOf(str.toLowerCase(Locale.ENGLISH)); } catch (Exception exception) { try { return valueOf("fg_" + str.toLowerCase(Locale.ENGLISH)); } catch (Exception exception1) { return new CommandLine.Help.Ansi.Palette256Color(true, str); }  }  } public static CommandLine.Help.Ansi.IStyle bg(String str) { try { return valueOf(str.toLowerCase(Locale.ENGLISH)); } catch (Exception exception) { try { return valueOf("bg_" + str.toLowerCase(Locale.ENGLISH)); } catch (Exception exception1) { return new CommandLine.Help.Ansi.Palette256Color(false, str); }  }  } public static CommandLine.Help.Ansi.IStyle[] parse(String commaSeparatedCodes) { String[] codes = commaSeparatedCodes.split(","); CommandLine.Help.Ansi.IStyle[] styles = new CommandLine.Help.Ansi.IStyle[codes.length]; for (int i = 0; i < codes.length; i++) { if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("fg(")) { int end = codes[i].indexOf(')'); styles[i] = fg(codes[i].substring(3, (end < 0) ? codes[i].length() : end)); } else if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("bg(")) { int end = codes[i].indexOf(')'); styles[i] = bg(codes[i].substring(3, (end < 0) ? codes[i].length() : end)); } else { styles[i] = fg(codes[i]); }  }  return styles; } } public class Text implements Cloneable { private final int maxLength; private int from; private int length; private StringBuilder plain = new StringBuilder(); private List<CommandLine.Help.Ansi.StyledSection> sections = new ArrayList<>(); public Text(int maxLength) { this.maxLength = maxLength; } public Text(String input) { this.maxLength = -1; this.plain.setLength(0); int i = 0; while (true) { int j = input.indexOf("@|", i); if (j == -1) { if (i == 0) { this.plain.append(input); this.length = this.plain.length(); return; }  this.plain.append(input.substring(i, input.length())); this.length = this.plain.length(); return; }  this.plain.append(input.substring(i, j)); int k = input.indexOf("|@", j); if (k == -1) { this.plain.append(input); this.length = this.plain.length(); return; }  j += 2; String spec = input.substring(j, k); String[] items = spec.split(" ", 2); if (items.length == 1) { this.plain.append(input); this.length = this.plain.length(); return; }  CommandLine.Help.Ansi.IStyle[] styles = CommandLine.Help.Ansi.Style.parse(items[0]); addStyledSection(this.plain.length(), items[1].length(), CommandLine.Help.Ansi.Style.on(styles), CommandLine.Help.Ansi.Style.off((CommandLine.Help.Ansi.IStyle[])CommandLine.Help.Ansi.reverse((T[])styles)) + CommandLine.Help.Ansi.Style.reset.off()); this.plain.append(items[1]); i = k + 2; }  } private void addStyledSection(int start, int length, String startStyle, String endStyle) { this.sections.add(new CommandLine.Help.Ansi.StyledSection(start, length, startStyle, endStyle)); } public Object clone() { try { return super.clone(); } catch (CloneNotSupportedException e) { throw new IllegalStateException(e); }  } public Text[] splitLines() { List<Text> result = new ArrayList<>(); boolean trailingEmptyString = false; int start = 0, end = 0; for (int i = 0; i < this.plain.length(); end = ++i) { char c = this.plain.charAt(i); boolean eol = (c == '\n'); int j = eol | ((c == '\r' && i + 1 < this.plain.length() && this.plain.charAt(i + 1) == '\n' && ++i > 0) ? 1 : 0); j |= (c == '\r') ? 1 : 0; if (j != 0) { result.add(substring(start, end)); trailingEmptyString = (i == this.plain.length() - 1); start = i + 1; }  }  if (start < this.plain.length() || trailingEmptyString) result.add(substring(start, this.plain.length()));  return result.<Text>toArray(new Text[result.size()]); } public Text substring(int start) { return substring(start, this.length); } public Text substring(int start, int end) { Text result = (Text)clone(); this.from += start; result.length = end - start; return result; } public Text append(String string) { return append(new Text(string)); } public Text append(Text other) { Text result = (Text)clone(); result.plain = new StringBuilder(this.plain.toString().substring(this.from, this.from + this.length)); result.from = 0; result.sections = new ArrayList<>(); for (CommandLine.Help.Ansi.StyledSection section : this.sections) result.sections.add(section.withStartIndex(section.startIndex - this.from));  result.plain.append(other.plain.toString().substring(other.from, other.from + other.length)); for (CommandLine.Help.Ansi.StyledSection section : other.sections) { int index = result.length + section.startIndex - other.from; result.sections.add(section.withStartIndex(index)); }  result.length = result.plain.length(); return result; } public void getStyledChars(int from, int length, Text destination, int offset) { if (destination.length < offset) { for (int i = destination.length; i < offset; i++) destination.plain.append(' ');  destination.length = offset; }  for (CommandLine.Help.Ansi.StyledSection section : this.sections) destination.sections.add(section.withStartIndex(section.startIndex - from + destination.length));  destination.plain.append(this.plain.toString().substring(from, from + length)); destination.length = destination.plain.length(); } public String plainString() { return this.plain.toString().substring(this.from, this.from + this.length); } public boolean equals(Object obj) { return toString().equals(String.valueOf(obj)); } public int hashCode() { return toString().hashCode(); } public String toString() { if (!CommandLine.Help.Ansi.this.enabled()) return this.plain.toString().substring(this.from, this.from + this.length);  if (this.length == 0) return "";  StringBuilder sb = new StringBuilder(this.plain.length() + 20 * this.sections.size()); CommandLine.Help.Ansi.StyledSection current = null; int end = Math.min(this.from + this.length, this.plain.length()); for (int i = this.from; i < end; i++) { CommandLine.Help.Ansi.StyledSection section = findSectionContaining(i); if (section != current) { if (current != null) sb.append(current.endStyles);  if (section != null) sb.append(section.startStyles);  current = section; }  sb.append(this.plain.charAt(i)); }  if (current != null) sb.append(current.endStyles);  return sb.toString(); } private CommandLine.Help.Ansi.StyledSection findSectionContaining(int index) { for (CommandLine.Help.Ansi.StyledSection section : this.sections) { if (index >= section.startIndex && index < section.startIndex + section.length) return section;  }  return null; } } public static interface IParamLabelRenderer { CommandLine.Help.Ansi.Text renderParameterLabel(Field param2Field, CommandLine.Help.Ansi param2Ansi, List<CommandLine.Help.Ansi.IStyle> param2List); String separator(); } public static interface IParameterRenderer { CommandLine.Help.Ansi.Text[][] render(CommandLine.Parameters param2Parameters, Field param2Field, CommandLine.Help.IParamLabelRenderer param2IParamLabelRenderer, CommandLine.Help.ColorScheme param2ColorScheme); } public static interface IOptionRenderer { CommandLine.Help.Ansi.Text[][] render(CommandLine.Option param2Option, Field param2Field, CommandLine.Help.IParamLabelRenderer param2IParamLabelRenderer, CommandLine.Help.ColorScheme param2ColorScheme); } public static interface IStyle { public static final String CSI = "\033["; String on(); String off(); } } public static class ColorScheme { public final List<CommandLine.Help.Ansi.IStyle> commandStyles = new ArrayList<>(); public final List<CommandLine.Help.Ansi.IStyle> optionStyles = new ArrayList<>(); public final List<CommandLine.Help.Ansi.IStyle> parameterStyles = new ArrayList<>(); public final List<CommandLine.Help.Ansi.IStyle> optionParamStyles = new ArrayList<>(); private final CommandLine.Help.Ansi ansi; public ColorScheme() { this(CommandLine.Help.Ansi.AUTO); } public ColorScheme(CommandLine.Help.Ansi ansi) { this.ansi = CommandLine.Assert.<CommandLine.Help.Ansi>notNull(ansi, "ansi"); } public ColorScheme commands(CommandLine.Help.Ansi.IStyle... styles) { return addAll(this.commandStyles, styles); } public ColorScheme options(CommandLine.Help.Ansi.IStyle... styles) { return addAll(this.optionStyles, styles); } public ColorScheme parameters(CommandLine.Help.Ansi.IStyle... styles) { return addAll(this.parameterStyles, styles); } public ColorScheme optionParams(CommandLine.Help.Ansi.IStyle... styles) { return addAll(this.optionParamStyles, styles); } public CommandLine.Help.Ansi.Text commandText(String command) { return ansi().apply(command, this.commandStyles); } public CommandLine.Help.Ansi.Text optionText(String option) { return ansi().apply(option, this.optionStyles); } public CommandLine.Help.Ansi.Text parameterText(String parameter) { return ansi().apply(parameter, this.parameterStyles); } public CommandLine.Help.Ansi.Text optionParamText(String optionParam) { return ansi().apply(optionParam, this.optionParamStyles); } public ColorScheme applySystemProperties() { replace(this.commandStyles, System.getProperty("picocli.color.commands")); replace(this.optionStyles, System.getProperty("picocli.color.options")); replace(this.parameterStyles, System.getProperty("picocli.color.parameters")); replace(this.optionParamStyles, System.getProperty("picocli.color.optionParams")); return this; } private void replace(List<CommandLine.Help.Ansi.IStyle> styles, String property) { if (property != null) { styles.clear(); addAll(styles, CommandLine.Help.Ansi.Style.parse(property)); }  } private ColorScheme addAll(List<CommandLine.Help.Ansi.IStyle> styles, CommandLine.Help.Ansi.IStyle... add) { styles.addAll(Arrays.asList(add)); return this; } public CommandLine.Help.Ansi ansi() { return this.ansi; } } public enum Ansi { AUTO, ON, OFF; static Text EMPTY_TEXT = new Text(0); static final boolean isWindows = System.getProperty("os.name").startsWith("Windows"); static final boolean isXterm = (System.getenv("TERM") != null && System.getenv("TERM").startsWith("xterm")); static final boolean ISATTY = calcTTY(); static {  } static final boolean calcTTY() { if (isWindows && isXterm) return true;  try { return (System.class.getDeclaredMethod("console", new Class[0]).invoke(null, new Object[0]) != null); } catch (Throwable reflectionFailed) { return true; }  } private static boolean ansiPossible() { return (ISATTY && (!isWindows || isXterm)); } public boolean enabled() { if (this == ON) return true;  if (this == OFF) return false;  return (System.getProperty("picocli.ansi") == null) ? ansiPossible() : Boolean.getBoolean("picocli.ansi"); } public static interface IStyle { public static final String CSI = "\033["; String on(); String off(); } public enum Style implements IStyle { reset(0, 0), bold(1, 21), faint(2, 22), italic(3, 23), underline(4, 24), blink(5, 25), reverse(7, 27), fg_black(30, 39), fg_red(31, 39), fg_green(32, 39), fg_yellow(33, 39), fg_blue(34, 39), fg_magenta(35, 39), fg_cyan(36, 39), fg_white(37, 39), bg_black(40, 49), bg_red(41, 49), bg_green(42, 49), bg_yellow(43, 49), bg_blue(44, 49), bg_magenta(45, 49), bg_cyan(46, 49), bg_white(47, 49); private final int startCode; private final int endCode; Style(int startCode, int endCode) { this.startCode = startCode; this.endCode = endCode; } public String on() { return "\033[" + this.startCode + "m"; } public String off() { return "\033[" + this.endCode + "m"; } public static String on(CommandLine.Help.Ansi.IStyle... styles) { StringBuilder result = new StringBuilder(); for (CommandLine.Help.Ansi.IStyle style : styles) result.append(style.on());  return result.toString(); } public static String off(CommandLine.Help.Ansi.IStyle... styles) { StringBuilder result = new StringBuilder(); for (CommandLine.Help.Ansi.IStyle style : styles) result.append(style.off());  return result.toString(); } public static CommandLine.Help.Ansi.IStyle fg(String str) { try { return valueOf(str.toLowerCase(Locale.ENGLISH)); } catch (Exception exception) { try { return valueOf("fg_" + str.toLowerCase(Locale.ENGLISH)); } catch (Exception exception1) { return new CommandLine.Help.Ansi.Palette256Color(true, str); }  }  } public static CommandLine.Help.Ansi.IStyle bg(String str) { try { return valueOf(str.toLowerCase(Locale.ENGLISH)); } catch (Exception exception) { try { return valueOf("bg_" + str.toLowerCase(Locale.ENGLISH)); } catch (Exception exception1) { return new CommandLine.Help.Ansi.Palette256Color(false, str); }  }  } public static CommandLine.Help.Ansi.IStyle[] parse(String commaSeparatedCodes) { String[] codes = commaSeparatedCodes.split(","); CommandLine.Help.Ansi.IStyle[] styles = new CommandLine.Help.Ansi.IStyle[codes.length]; for (int i = 0; i < codes.length; i++) { if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("fg(")) { int end = codes[i].indexOf(')'); styles[i] = fg(codes[i].substring(3, (end < 0) ? codes[i].length() : end)); } else if (codes[i].toLowerCase(Locale.ENGLISH).startsWith("bg(")) { int end = codes[i].indexOf(')'); styles[i] = bg(codes[i].substring(3, (end < 0) ? codes[i].length() : end)); } else { styles[i] = fg(codes[i]); }  }  return styles; } } static class Palette256Color implements IStyle { private final int fgbg; private final int color; Palette256Color(boolean foreground, String color) { this.fgbg = foreground ? 38 : 48; String[] rgb = color.split(";"); if (rgb.length == 3) { this.color = 16 + 36 * Integer.decode(rgb[0]).intValue() + 6 * Integer.decode(rgb[1]).intValue() + Integer.decode(rgb[2]).intValue(); } else { this.color = Integer.decode(color).intValue(); }  } public String on() { return String.format("\033[%d;5;%dm", new Object[] { Integer.valueOf(this.fgbg), Integer.valueOf(this.color) }); } public String off() { return "\033[" + (this.fgbg + 1) + "m"; } } private static class StyledSection { int startIndex; int length; String startStyles; String endStyles; StyledSection(int start, int len, String style1, String style2) { this.startIndex = start; this.length = len; this.startStyles = style1; this.endStyles = style2; } StyledSection withStartIndex(int newStart) { return new StyledSection(newStart, this.length, this.startStyles, this.endStyles); } } public Text apply(String plainText, List<IStyle> styles) { if (plainText.length() == 0) return new Text(0);  Text result = new Text(plainText.length()); IStyle[] all = styles.<IStyle>toArray(new IStyle[styles.size()]); result.sections.add(new StyledSection(0, plainText.length(), Style.on(all), Style.off(reverse(all)) + Style.reset.off())); result.plain.append(plainText); result.length = result.plain.length(); return result; } private static <T> T[] reverse(T[] all) { for (int i = 0; i < all.length / 2; i++) { T temp = all[i]; all[i] = all[all.length - i - 1]; all[all.length - i - 1] = temp; }  return all; } public class Text implements Cloneable { private final int maxLength; private int from; private int length; private StringBuilder plain = new StringBuilder(); private CommandLine.Help.Ansi.StyledSection findSectionContaining(int index) { for (CommandLine.Help.Ansi.StyledSection section : this.sections) { if (index >= section.startIndex && index < section.startIndex + section.length) return section;  }  return null; } private List<CommandLine.Help.Ansi.StyledSection> sections = new ArrayList<>(); public Text(int maxLength) { this.maxLength = maxLength; } public Text(String input) { this.maxLength = -1; this.plain.setLength(0); int i = 0; while (true) { int j = input.indexOf("@|", i); if (j == -1) { if (i == 0) { this.plain.append(input); this.length = this.plain.length(); return; }  this.plain.append(input.substring(i, input.length())); this.length = this.plain.length(); return; }  this.plain.append(input.substring(i, j)); int k = input.indexOf("|@", j); if (k == -1) { this.plain.append(input); this.length = this.plain.length(); return; }  j += 2; String spec = input.substring(j, k); String[] items = spec.split(" ", 2); if (items.length == 1) { this.plain.append(input); this.length = this.plain.length(); return; }  CommandLine.Help.Ansi.IStyle[] styles = CommandLine.Help.Ansi.Style.parse(items[0]); addStyledSection(this.plain.length(), items[1].length(), CommandLine.Help.Ansi.Style.on(styles), CommandLine.Help.Ansi.Style.off((CommandLine.Help.Ansi.IStyle[])CommandLine.Help.Ansi.reverse((T[])styles)) + CommandLine.Help.Ansi.Style.reset.off()); this.plain.append(items[1]); i = k + 2; }  } private void addStyledSection(int start, int length, String startStyle, String endStyle) { this.sections.add(new CommandLine.Help.Ansi.StyledSection(start, length, startStyle, endStyle)); } public Object clone() { try { return super.clone(); } catch (CloneNotSupportedException e) { throw new IllegalStateException(e); }  } public Text[] splitLines() { List<Text> result = new ArrayList<>(); boolean trailingEmptyString = false; int start = 0, end = 0; for (int i = 0; i < this.plain.length(); end = ++i) { char c = this.plain.charAt(i); boolean eol = (c == '\n'); int j = eol | ((c == '\r' && i + 1 < this.plain.length() && this.plain.charAt(i + 1) == '\n' && ++i > 0) ? 1 : 0); j |= (c == '\r') ? 1 : 0; if (j != 0) { result.add(substring(start, end)); trailingEmptyString = (i == this.plain.length() - 1); start = i + 1; }  }  if (start < this.plain.length() || trailingEmptyString) result.add(substring(start, this.plain.length()));  return result.<Text>toArray(new Text[result.size()]); } public Text substring(int start) { return substring(start, this.length); } public Text substring(int start, int end) { Text result = (Text)clone(); this.from += start; result.length = end - start; return result; } public Text append(String string) { return append(new Text(string)); } public Text append(Text other) { Text result = (Text)clone(); result.plain = new StringBuilder(this.plain.toString().substring(this.from, this.from + this.length)); result.from = 0; result.sections = new ArrayList<>(); for (CommandLine.Help.Ansi.StyledSection section : this.sections) result.sections.add(section.withStartIndex(section.startIndex - this.from));  result.plain.append(other.plain.toString().substring(other.from, other.from + other.length)); for (CommandLine.Help.Ansi.StyledSection section : other.sections) { int index = result.length + section.startIndex - other.from; result.sections.add(section.withStartIndex(index)); }  result.length = result.plain.length(); return result; } public void getStyledChars(int from, int length, Text destination, int offset) { if (destination.length < offset) { for (int i = destination.length; i < offset; i++) destination.plain.append(' ');  destination.length = offset; }  for (CommandLine.Help.Ansi.StyledSection section : this.sections) destination.sections.add(section.withStartIndex(section.startIndex - from + destination.length));  destination.plain.append(this.plain.toString().substring(from, from + length)); destination.length = destination.plain.length(); } public String plainString() { return this.plain.toString().substring(this.from, this.from + this.length); } public boolean equals(Object obj) { return toString().equals(String.valueOf(obj)); } public int hashCode() { return toString().hashCode(); } public String toString() { if (!CommandLine.Help.Ansi.this.enabled()) return this.plain.toString().substring(this.from, this.from + this.length);  if (this.length == 0) return "";  StringBuilder sb = new StringBuilder(this.plain.length() + 20 * this.sections.size()); CommandLine.Help.Ansi.StyledSection current = null; int end = Math.min(this.from + this.length, this.plain.length()); for (int i = this.from; i < end; i++) { CommandLine.Help.Ansi.StyledSection section = findSectionContaining(i); if (section != current) { if (current != null) sb.append(current.endStyles);  if (section != null) sb.append(section.startStyles);  current = section; }  sb.append(this.plain.charAt(i)); }  if (current != null) sb.append(current.endStyles);  return sb.toString(); } } } public class Text implements Cloneable { private final int maxLength; private int from; private int length; private StringBuilder plain = new StringBuilder(); private List<CommandLine.Help.Ansi.StyledSection> sections = new ArrayList<>(); public Text(int maxLength) { this.maxLength = maxLength; } public Text(String input) { this.maxLength = -1; this.plain.setLength(0); int i = 0; while (true) { int j = input.indexOf("@|", i); if (j == -1) { if (i == 0) { this.plain.append(input); this.length = this.plain.length(); return; }  this.plain.append(input.substring(i, input.length())); this.length = this.plain.length(); return; }  this.plain.append(input.substring(i, j)); int k = input.indexOf("|@", j); if (k == -1) { this.plain.append(input); this.length = this.plain.length(); return; }  j += 2; String spec = input.substring(j, k); String[] items = spec.split(" ", 2); if (items.length == 1) { this.plain.append(input); this.length = this.plain.length(); return; }  CommandLine.Help.Ansi.IStyle[] styles = CommandLine.Help.Ansi.Style.parse(items[0]); addStyledSection(this.plain.length(), items[1].length(), CommandLine.Help.Ansi.Style.on(styles), CommandLine.Help.Ansi.Style.off((CommandLine.Help.Ansi.IStyle[])CommandLine.Help.Ansi.reverse((T[])styles)) + CommandLine.Help.Ansi.Style.reset.off()); this.plain.append(items[1]); i = k + 2; }  } private void addStyledSection(int start, int length, String startStyle, String endStyle) { this.sections.add(new CommandLine.Help.Ansi.StyledSection(start, length, startStyle, endStyle)); } public Object clone() { try { return super.clone(); } catch (CloneNotSupportedException e) { throw new IllegalStateException(e); }  } public Text[] splitLines() { List<Text> result = new ArrayList<>(); boolean trailingEmptyString = false; int start = 0, end = 0; for (int i = 0; i < this.plain.length(); end = ++i) { char c = this.plain.charAt(i); boolean eol = (c == '\n'); int j = eol | ((c == '\r' && i + 1 < this.plain.length() && this.plain.charAt(i + 1) == '\n' && ++i > 0) ? 1 : 0); j |= (c == '\r') ? 1 : 0; if (j != 0) { result.add(substring(start, end)); trailingEmptyString = (i == this.plain.length() - 1); start = i + 1; }  }  if (start < this.plain.length() || trailingEmptyString) result.add(substring(start, this.plain.length()));  return result.<Text>toArray(new Text[result.size()]); } public Text substring(int start) { return substring(start, this.length); } public Text substring(int start, int end) { Text result = (Text)clone(); this.from += start; result.length = end - start; return result; } public Text append(String string) { return append(new Text(string)); } public Text append(Text other) { Text result = (Text)clone(); result.plain = new StringBuilder(this.plain.toString().substring(this.from, this.from + this.length)); result.from = 0; result.sections = new ArrayList<>(); for (CommandLine.Help.Ansi.StyledSection section : this.sections) result.sections.add(section.withStartIndex(section.startIndex - this.from));  result.plain.append(other.plain.toString().substring(other.from, other.from + other.length)); for (CommandLine.Help.Ansi.StyledSection section : other.sections) { int index = result.length + section.startIndex - other.from; result.sections.add(section.withStartIndex(index)); }  result.length = result.plain.length(); return result; } public void getStyledChars(int from, int length, Text destination, int offset) { if (destination.length < offset) { for (int i = destination.length; i < offset; i++) destination.plain.append(' ');  destination.length = offset; }  for (CommandLine.Help.Ansi.StyledSection section : this.sections) destination.sections.add(section.withStartIndex(section.startIndex - from + destination.length));  destination.plain.append(this.plain.toString().substring(from, from + length)); destination.length = destination.plain.length(); } public String plainString() { return this.plain.toString().substring(this.from, this.from + this.length); } public boolean equals(Object obj) { return toString().equals(String.valueOf(obj)); } public int hashCode() { return toString().hashCode(); } public String toString() { if (!CommandLine.Help.Ansi.this.enabled()) return this.plain.toString().substring(this.from, this.from + this.length);  if (this.length == 0) return "";  StringBuilder sb = new StringBuilder(this.plain.length() + 20 * this.sections.size()); CommandLine.Help.Ansi.StyledSection current = null; int end = Math.min(this.from + this.length, this.plain.length()); for (int i = this.from; i < end; i++) { CommandLine.Help.Ansi.StyledSection section = findSectionContaining(i); if (section != current) { if (current != null) sb.append(current.endStyles);  if (section != null) sb.append(section.startStyles);  current = section; }  sb.append(this.plain.charAt(i)); }  if (current != null) sb.append(current.endStyles);  return sb.toString(); } private CommandLine.Help.Ansi.StyledSection findSectionContaining(int index) { for (CommandLine.Help.Ansi.StyledSection section : this.sections) { if (index >= section.startIndex && index < section.startIndex + section.length) return section;  }  return null; }
/*      */      }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */   
/*      */   private static final class Assert
/*      */   {
/*      */     static <T> T notNull(T object, String description) {
/* 4650 */       if (object == null) {
/* 4651 */         throw new NullPointerException(description);
/*      */       }
/* 4653 */       return object;
/*      */     }
/*      */   }
/*      */   
/* 4657 */   private enum TraceLevel { OFF, WARN, INFO, DEBUG; public boolean isEnabled(TraceLevel other) {
/* 4658 */       return (ordinal() >= other.ordinal());
/*      */     } private void print(CommandLine.Tracer tracer, String msg, Object... params) {
/* 4660 */       if (tracer.level.isEnabled(this)) tracer.stream.printf(prefix(msg), params); 
/*      */     }
/* 4662 */     private String prefix(String msg) { return "[picocli " + this + "] " + msg; } static TraceLevel lookup(String key) {
/* 4663 */       return (key == null) ? WARN : ((CommandLine.empty(key) || "true".equalsIgnoreCase(key)) ? INFO : valueOf(key));
/*      */     } }
/*      */   
/* 4666 */   private static class Tracer { CommandLine.TraceLevel level = CommandLine.TraceLevel.lookup(System.getProperty("picocli.trace"));
/* 4667 */     PrintStream stream = System.err;
/* 4668 */     void warn(String msg, Object... params) { CommandLine.TraceLevel.WARN.print(this, msg, params); }
/* 4669 */     void info(String msg, Object... params) { CommandLine.TraceLevel.INFO.print(this, msg, params); }
/* 4670 */     void debug(String msg, Object... params) { CommandLine.TraceLevel.DEBUG.print(this, msg, params); }
/* 4671 */     boolean isWarn() { return this.level.isEnabled(CommandLine.TraceLevel.WARN); }
/* 4672 */     boolean isInfo() { return this.level.isEnabled(CommandLine.TraceLevel.INFO); } boolean isDebug() {
/* 4673 */       return this.level.isEnabled(CommandLine.TraceLevel.DEBUG);
/*      */     }
/*      */     private Tracer() {} }
/*      */   
/*      */   public static class PicocliException extends RuntimeException { private static final long serialVersionUID = -2574128880125050818L;
/*      */     
/* 4679 */     public PicocliException(String msg) { super(msg); } public PicocliException(String msg, Exception ex) {
/* 4680 */       super(msg, ex);
/*      */     } }
/*      */   
/*      */   public static class InitializationException extends PicocliException {
/*      */     private static final long serialVersionUID = 8423014001666638895L;
/*      */     
/* 4686 */     public InitializationException(String msg) { super(msg); } public InitializationException(String msg, Exception ex) {
/* 4687 */       super(msg, ex);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class ExecutionException extends PicocliException { private static final long serialVersionUID = 7764539594267007998L;
/*      */     private final CommandLine commandLine;
/*      */     
/*      */     public ExecutionException(CommandLine commandLine, String msg) {
/* 4695 */       super(msg);
/* 4696 */       this.commandLine = CommandLine.Assert.<CommandLine>notNull(commandLine, "commandLine");
/*      */     }
/*      */     public ExecutionException(CommandLine commandLine, String msg, Exception ex) {
/* 4699 */       super(msg, ex);
/* 4700 */       this.commandLine = CommandLine.Assert.<CommandLine>notNull(commandLine, "commandLine");
/*      */     }
/*      */ 
/*      */     
/*      */     public CommandLine getCommandLine() {
/* 4705 */       return this.commandLine;
/*      */     } }
/*      */   
/*      */   public static class TypeConversionException extends PicocliException { private static final long serialVersionUID = 4251973913816346114L;
/*      */     
/*      */     public TypeConversionException(String msg) {
/* 4711 */       super(msg);
/*      */     } }
/*      */ 
/*      */ 
/*      */   
/*      */   public static class ParameterException
/*      */     extends PicocliException
/*      */   {
/*      */     private static final long serialVersionUID = 1477112829129763139L;
/*      */     private final CommandLine commandLine;
/*      */     
/*      */     public ParameterException(CommandLine commandLine, String msg) {
/* 4723 */       super(msg);
/* 4724 */       this.commandLine = CommandLine.Assert.<CommandLine>notNull(commandLine, "commandLine");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public ParameterException(CommandLine commandLine, String msg, Exception ex) {
/* 4732 */       super(msg, ex);
/* 4733 */       this.commandLine = CommandLine.Assert.<CommandLine>notNull(commandLine, "commandLine");
/*      */     }
/*      */ 
/*      */ 
/*      */ 
/*      */     
/*      */     public CommandLine getCommandLine() {
/* 4740 */       return this.commandLine;
/*      */     }
/*      */     
/*      */     private static ParameterException create(CommandLine cmd, Exception ex, String arg, int i, String[] args) {
/* 4744 */       String msg = ex.getClass().getSimpleName() + ": " + ex.getLocalizedMessage() + " while processing argument at or before arg[" + i + "] '" + arg + "' in " + Arrays.toString((Object[])args) + ": " + ex.toString();
/* 4745 */       return new ParameterException(cmd, msg, ex);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class MissingParameterException
/*      */     extends ParameterException {
/*      */     private static final long serialVersionUID = 5075678535706338753L;
/*      */     
/*      */     public MissingParameterException(CommandLine commandLine, String msg) {
/* 4754 */       super(commandLine, msg);
/*      */     }
/*      */     
/*      */     private static MissingParameterException create(CommandLine cmd, Collection<Field> missing, String separator) {
/* 4758 */       if (missing.size() == 1) {
/* 4759 */         return new MissingParameterException(cmd, "Missing required option '" + 
/* 4760 */             describe(missing.iterator().next(), separator) + "'");
/*      */       }
/* 4762 */       List<String> names = new ArrayList<>(missing.size());
/* 4763 */       for (Field field : missing) {
/* 4764 */         names.add(describe(field, separator));
/*      */       }
/* 4766 */       return new MissingParameterException(cmd, "Missing required options " + names.toString());
/*      */     }
/*      */ 
/*      */     
/*      */     private static String describe(Field field, String separator) {
/* 4771 */       String prefix = field.isAnnotationPresent((Class)CommandLine.Option.class) ? (((CommandLine.Option)field.getAnnotation((Class)CommandLine.Option.class)).names()[0] + separator) : ("params[" + ((CommandLine.Parameters)field.<CommandLine.Parameters>getAnnotation(CommandLine.Parameters.class)).index() + "]" + separator);
/* 4772 */       return prefix + CommandLine.Help.DefaultParamLabelRenderer.renderParameterName(field);
/*      */     }
/*      */   }
/*      */   
/*      */   public static class DuplicateOptionAnnotationsException
/*      */     extends InitializationException {
/*      */     private static final long serialVersionUID = -3355128012575075641L;
/*      */     
/*      */     public DuplicateOptionAnnotationsException(String msg) {
/* 4781 */       super(msg);
/*      */     }
/*      */     private static DuplicateOptionAnnotationsException create(String name, Field field1, Field field2) {
/* 4784 */       return new DuplicateOptionAnnotationsException("Option name '" + name + "' is used by both " + field1
/* 4785 */           .getDeclaringClass().getName() + "." + field1.getName() + " and " + field2
/* 4786 */           .getDeclaringClass().getName() + "." + field2.getName());
/*      */     } }
/*      */   
/*      */   public static class ParameterIndexGapException extends InitializationException { private static final long serialVersionUID = -1520981133257618319L;
/*      */     
/*      */     public ParameterIndexGapException(String msg) {
/* 4792 */       super(msg);
/*      */     } }
/*      */ 
/*      */   
/*      */   public static class UnmatchedArgumentException extends ParameterException { private static final long serialVersionUID = -8700426380701452440L;
/*      */     
/* 4798 */     public UnmatchedArgumentException(CommandLine commandLine, String msg) { super(commandLine, msg); }
/* 4799 */     public UnmatchedArgumentException(CommandLine commandLine, Stack<String> args) { this(commandLine, new ArrayList<>((Collection)CommandLine.reverse((Stack)args))); } public UnmatchedArgumentException(CommandLine commandLine, List<String> args) {
/* 4800 */       this(commandLine, "Unmatched argument" + ((args.size() == 1) ? " " : "s ") + args);
/*      */     } }
/*      */   public static class MaxValuesforFieldExceededException extends ParameterException { private static final long serialVersionUID = 6536145439570100641L;
/*      */     
/*      */     public MaxValuesforFieldExceededException(CommandLine commandLine, String msg) {
/* 4805 */       super(commandLine, msg);
/*      */     } }
/*      */   public static class OverwrittenOptionException extends ParameterException { private static final long serialVersionUID = 1338029208271055776L;
/*      */     
/*      */     public OverwrittenOptionException(CommandLine commandLine, String msg) {
/* 4810 */       super(commandLine, msg);
/*      */     } }
/*      */ 
/*      */   
/*      */   public static class MissingTypeConverterException extends ParameterException {
/*      */     private static final long serialVersionUID = -6050931703233083760L;
/*      */     
/*      */     public MissingTypeConverterException(CommandLine commandLine, String msg) {
/* 4818 */       super(commandLine, msg);
/*      */     }
/*      */   }
/*      */   
/*      */   public static interface ITypeConverter<K> {
/*      */     K convert(String param1String) throws Exception;
/*      */   }
/*      */   
/*      */   @Retention(RetentionPolicy.RUNTIME)
/*      */   @Target({ElementType.TYPE, ElementType.LOCAL_VARIABLE, ElementType.PACKAGE})
/*      */   public static @interface Command {
/*      */     String name() default "<main class>";
/*      */     
/*      */     Class<?>[] subcommands() default {};
/*      */     
/*      */     String separator() default "=";
/*      */     
/*      */     String[] version() default {};
/*      */     
/*      */     String headerHeading() default "";
/*      */     
/*      */     String[] header() default {};
/*      */     
/*      */     String synopsisHeading() default "Usage: ";
/*      */     
/*      */     boolean abbreviateSynopsis() default false;
/*      */     
/*      */     String[] customSynopsis() default {};
/*      */     
/*      */     String descriptionHeading() default "";
/*      */     
/*      */     String[] description() default {};
/*      */     
/*      */     String parameterListHeading() default "";
/*      */     
/*      */     String optionListHeading() default "";
/*      */     
/*      */     boolean sortOptions() default true;
/*      */     
/*      */     char requiredOptionMarker() default ' ';
/*      */     
/*      */     boolean showDefaultValues() default false;
/*      */     
/*      */     String commandListHeading() default "Commands:%n";
/*      */     
/*      */     String footerHeading() default "";
/*      */     
/*      */     String[] footer() default {};
/*      */   }
/*      */   
/*      */   @Retention(RetentionPolicy.RUNTIME)
/*      */   @Target({ElementType.FIELD})
/*      */   public static @interface Parameters {
/*      */     String index() default "*";
/*      */     
/*      */     String[] description() default {};
/*      */     
/*      */     String arity() default "";
/*      */     
/*      */     String paramLabel() default "";
/*      */     
/*      */     Class<?>[] type() default {};
/*      */     
/*      */     String split() default "";
/*      */     
/*      */     boolean hidden() default false;
/*      */   }
/*      */   
/*      */   @Retention(RetentionPolicy.RUNTIME)
/*      */   @Target({ElementType.FIELD})
/*      */   public static @interface Option {
/*      */     String[] names();
/*      */     
/*      */     boolean required() default false;
/*      */     
/*      */     boolean help() default false;
/*      */     
/*      */     boolean usageHelp() default false;
/*      */     
/*      */     boolean versionHelp() default false;
/*      */     
/*      */     String[] description() default {};
/*      */     
/*      */     String arity() default "";
/*      */     
/*      */     String paramLabel() default "";
/*      */     
/*      */     Class<?>[] type() default {};
/*      */     
/*      */     String split() default "";
/*      */     
/*      */     boolean hidden() default false;
/*      */   }
/*      */   
/*      */   public static interface IExceptionHandler {
/*      */     List<Object> handleException(CommandLine.ParameterException param1ParameterException, PrintStream param1PrintStream, CommandLine.Help.Ansi param1Ansi, String... param1VarArgs);
/*      */   }
/*      */   
/*      */   public static interface IParseResultHandler {
/*      */     List<Object> handleParseResult(List<CommandLine> param1List, PrintStream param1PrintStream, CommandLine.Help.Ansi param1Ansi) throws CommandLine.ExecutionException;
/*      */   }
/*      */   
/*      */   public static interface IParamLabelRenderer {
/*      */     CommandLine.Help.Ansi.Text renderParameterLabel(Field param1Field, CommandLine.Help.Ansi param1Ansi, List<CommandLine.Help.Ansi.IStyle> param1List);
/*      */     
/*      */     String separator();
/*      */   }
/*      */ }


/* Location:              E:\莫单人萨米\desktop-1.0.jar!\org\apache\logging\log4j\core\tools\picocli\CommandLine.class
 * Java compiler version: 8 (52.0)
 * JD-Core Version:       1.1.3
 */