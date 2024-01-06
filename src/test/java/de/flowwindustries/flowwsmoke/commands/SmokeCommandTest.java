package de.flowwindustries.flowwsmoke.commands;

import de.flowwindustries.flowwsmoke.domain.SmokeLocation;
import de.flowwindustries.flowwsmoke.lang.LanguageLoader;
import de.flowwindustries.flowwsmoke.service.SmokeLocationService;
import org.assertj.core.api.Assertions;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static de.flowwindustries.flowwsmoke.service.SmokeLocationServiceImplTest.DUMMY_FREQUENCY;
import static de.flowwindustries.flowwsmoke.service.SmokeLocationServiceImplTest.DUMMY_WORLD;
import static de.flowwindustries.flowwsmoke.service.SmokeLocationServiceImplTest.DUMMY_X;
import static de.flowwindustries.flowwsmoke.service.SmokeLocationServiceImplTest.DUMMY_Y;
import static de.flowwindustries.flowwsmoke.service.SmokeLocationServiceImplTest.DUMMY_Z;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class SmokeCommandTest {
    
    private static final String LABEL_MOCK = "dummySmokeCommand";
    private final Block blockMock = mock(Block.class);
    private final World worldMock = mock(World.class);
    private final Player playerMock = mock(Player.class);
    private final Command commandMock = mock(Command.class);
    private final SmokeLocationService locationServiceMock = mock(SmokeLocationService.class);

    // Unit-under-test
    private final SmokeCommand smokeCommand = new SmokeCommand("test.smoke", locationServiceMock, DUMMY_FREQUENCY, "[FlowwSmoke]");

    @BeforeEach
    void setUp() {
        // to verify that the translations are set up properly for tests
        final var file = new File("./src/main/resources/lang/en-EN.yml");
        LanguageLoader.init(file.toPath());
        Assertions.assertThat(LanguageLoader.getMessage("messages.errors.unknown-arguments")).isEqualTo("Unknown Argument: {argument}");

        when(playerMock.hasPermission(eq("test.smoke"))).thenReturn(true);
        when(playerMock.getTargetBlockExact(eq(5))).thenReturn(blockMock);
        when(blockMock.getX()).thenReturn(DUMMY_X.intValue());
        when(blockMock.getY()).thenReturn(DUMMY_Y.intValue());
        when(blockMock.getZ()).thenReturn(DUMMY_Z.intValue());
        when(blockMock.getWorld()).thenReturn(worldMock);
        when(worldMock.getName()).thenReturn(DUMMY_WORLD);
    }

    @Test
    void verifyPlayerCommand() {
        // GIVEN
        var args = new String[]{"help"};
        // WHEN
        smokeCommand.onCommand(playerMock, commandMock, LABEL_MOCK, args);
        // THEN
        verify(playerMock, times(1)).sendMessage(contains(LanguageLoader.getMessage("messages.smoke.help")));
    }

    @Test
    void verifyList() {
        // GIVEN
        var args = new String[]{"list"};
        // WHEN
        smokeCommand.onCommand(playerMock, commandMock, LABEL_MOCK, args);
        // THEN
        verify(locationServiceMock, times(1)).getAll(null);
    }

    @Test
    void verifyListWorld() {
        // GIVEN
        var args = new String[]{"list", "world"};
        // WHEN
        smokeCommand.onCommand(playerMock, commandMock, LABEL_MOCK, args);
        // THEN
        verify(locationServiceMock, times(1)).getAll(eq("world"));
    }

    @Test
    void verifyRemoveSmoke() {
        // GIVEN
        var args = new String[]{"remove", "1"};
        // WHEN
        smokeCommand.onCommand(playerMock, commandMock, LABEL_MOCK, args);
        // THEN
        verify(locationServiceMock, times(1)).deleteSmoke(anyInt());
    }

    @Test
    void verifyRemoveSmokeAll() {
        // GIVEN
        var args = new String[]{"remove", "all"};
        // WHEN
        smokeCommand.onCommand(playerMock, commandMock, LABEL_MOCK, args);
        // THEN
        verify(locationServiceMock, times(1)).deleteAll(null);
    }

    @Test
    void verifyRemoveSmokeAllWorld() {
        // GIVEN
        var args = new String[]{"remove", "all", "world"};
        // WHEN
        smokeCommand.onCommand(playerMock, commandMock, LABEL_MOCK, args);
        // THEN
        verify(locationServiceMock, times(1)).deleteAll(eq("world"));
    }

    @Test
    void verifySmokeCreation() {
        // GIVEN
        var args = new String[]{};
        // WHEN
        smokeCommand.onCommand(playerMock, commandMock, LABEL_MOCK, args);
        // THEN
        verify(locationServiceMock, times(1)).addSmoke(eq(new SmokeLocation()
                .withWorldName(DUMMY_WORLD)
                .withFrequency(DUMMY_FREQUENCY) // as specified by config
                .withX(DUMMY_X.intValue() + 0.5d)
                .withY(DUMMY_Y.intValue() + 0.5d)
                .withZ(DUMMY_Z.intValue() + 0.5d)));
    }

    @Test
    void verifySmokeCreationWithCustomFrequency() {
        // GIVEN
        var args = new String[]{"add", "10"}; // add smoke with frequency of 10
        // WHEN
        smokeCommand.onCommand(playerMock, commandMock, LABEL_MOCK, args);
        // THEN
        verify(locationServiceMock, times(1)).addSmoke(eq(new SmokeLocation()
                .withWorldName(DUMMY_WORLD)
                .withFrequency(10) // as specified by args
                .withX(DUMMY_X.intValue() + 0.5d)
                .withY(DUMMY_Y.intValue() + 0.5d)
                .withZ(DUMMY_Z.intValue() + 0.5d)));
    }

    @Test
    void verifySmokeCreationWithIllegalFrequency() {
        var args = new String[]{"add", "66.66"};
        smokeCommand.onCommand(playerMock, commandMock, LABEL_MOCK, args);
        verify(playerMock, times(1)).sendMessage(contains("Unable to parse argument: 66.66"));
    }

    @Test
    void verifySmokeCreationFrequencyBelow1() {
        var args = new String[]{"add", "-1"};
        smokeCommand.onCommand(playerMock, commandMock, LABEL_MOCK, args);
        verify(playerMock, times(1)).sendMessage(contains("Spawn-Frequency must not be < 1"));
    }

    @Test
    void verifyInvalidArgs() {
        // GIVEN
        List<String[]> argsList = new ArrayList<>();
        argsList.add(new String[]{"lorem"});
        argsList.add(new String[]{"help", "me"});
        argsList.add(new String[]{"list", "world", "world"});
        argsList.add(new String[]{"remove", "1", "world"});

        // WHEN
        argsList.forEach(args -> smokeCommand.onCommand(playerMock, commandMock, LABEL_MOCK, args));

        // THEN
        verify(playerMock, times(argsList.size())).sendMessage(contains(LanguageLoader.getMessage("messages.errors.unknown-arguments").replace("{argument}", "")));
    }

    @Test
    void verifyUnknownNumberOfArgs() {
        // GIVEN
        var args = new String[]{"help", "me", "please", "lorem", "ipsum", "fizz"};
        // WHEN
        smokeCommand.onCommand(playerMock, commandMock, LABEL_MOCK, args);
        // THEN
        verify(playerMock, times(1)).sendMessage(contains(LanguageLoader.getMessage("messages.errors.unknown-arguments-length").replace("{arguments_length}", String.valueOf(args.length))));
    }
}
