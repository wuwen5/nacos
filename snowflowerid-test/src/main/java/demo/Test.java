package demo;

import ch.qos.logback.classic.Level;
import com.alibaba.nacos.sys.env.EnvUtil;
import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.slf4j.LoggerFactory;
import org.springframework.core.env.StandardEnvironment;

import java.util.concurrent.TimeUnit;

@State(Scope.Benchmark)
public class Test {

    static {
        EnvUtil.setEnvironment(new StandardEnvironment());
        ch.qos.logback.classic.Logger logger = (ch.qos.logback.classic.Logger) LoggerFactory.getLogger("root");

        logger.setLevel(Level.OFF);
    }

    public static void main(String[] args) throws RunnerException {
        
        Options opt = new OptionsBuilder()
                .include(Test.class.getSimpleName())
                .warmupIterations(3)
                .measurementIterations(5)
                .threads(1)
                .forks(1)
                .build();

        new Runner(opt).run();

    }

    NanoSnowFlowerIdGenerator idGenerator = new NanoSnowFlowerIdGenerator();

    SnowFlowerIdGenerator old = new SnowFlowerIdGenerator();

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void newImplUseNanoTimeCalc() {
        idGenerator.nextId();
    }

    @Benchmark
    @BenchmarkMode(Mode.Throughput)
    @OutputTimeUnit(TimeUnit.SECONDS)
    public void originally() {
        old.nextId();
    }
}
