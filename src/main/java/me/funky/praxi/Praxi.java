package me.funky.praxi;

import com.bizarrealex.aether.Aether;
import com.bizarrealex.aether.AetherOptions;
import me.funky.praxi.arena.Arena;
import me.funky.praxi.arena.ArenaListener;
import me.funky.praxi.arena.ArenaType;
import me.funky.praxi.arena.ArenaTypeAdapter;
import me.funky.praxi.arena.ArenaTypeTypeAdapter;
import me.funky.praxi.commands.admin.general.SetSpawnCommand;
import me.funky.praxi.commands.admin.arena.ArenaAddKitCommand;
import me.funky.praxi.commands.admin.arena.ArenaCreateCommand;
import me.funky.praxi.commands.admin.arena.ArenaDeleteCommand;
import me.funky.praxi.commands.admin.arena.ArenaGenHelperCommand;
import me.funky.praxi.commands.admin.arena.ArenaGenerateCommand;
import me.funky.praxi.commands.admin.arena.ArenaRemoveKitCommand;
import me.funky.praxi.commands.admin.arena.ArenaSaveCommand;
import me.funky.praxi.commands.admin.arena.ArenaSelectionCommand;
import me.funky.praxi.commands.admin.arena.ArenaSetSpawnCommand;
import me.funky.praxi.commands.admin.arena.ArenaStatusCommand;
import me.funky.praxi.commands.admin.arena.ArenasCommand;
import me.funky.praxi.commands.user.gamer.SuicideCommand;
import me.funky.praxi.essentials.Essentials;
import me.funky.praxi.event.Event;
import me.funky.praxi.event.EventTypeAdapter;
import me.funky.praxi.commands.event.admin.EventAdminCommand;
import me.funky.praxi.commands.event.admin.EventHelpCommand;
import me.funky.praxi.commands.event.admin.EventSetLobbyCommand;
import me.funky.praxi.event.game.EventGameListener;
import me.funky.praxi.commands.event.user.EventCancelCommand;
import me.funky.praxi.commands.event.user.EventClearCooldownCommand;
import me.funky.praxi.commands.event.user.EventForceStartCommand;
import me.funky.praxi.commands.event.user.EventHostCommand;
import me.funky.praxi.commands.event.user.EventInfoCommand;
import me.funky.praxi.commands.event.user.EventJoinCommand;
import me.funky.praxi.commands.event.user.EventLeaveCommand;
import me.funky.praxi.commands.event.admin.EventsCommand;
import me.funky.praxi.event.game.map.EventGameMap;
import me.funky.praxi.event.game.map.EventGameMapTypeAdapter;
import me.funky.praxi.commands.event.map.EventMapCreateCommand;
import me.funky.praxi.commands.event.map.EventMapDeleteCommand;
import me.funky.praxi.commands.event.map.EventMapSetSpawnCommand;
import me.funky.praxi.commands.event.map.EventMapStatusCommand;
import me.funky.praxi.commands.event.map.EventMapsCommand;
import me.funky.praxi.commands.event.admin.EventAddMapCommand;
import me.funky.praxi.commands.event.admin.EventRemoveMapCommand;
import me.funky.praxi.commands.event.vote.EventMapVoteCommand;
import me.funky.praxi.commands.admin.match.MatchTestCommand;
import me.funky.praxi.commands.user.match.ViewInventoryCommand;
import me.funky.praxi.commands.user.duels.DuelAcceptCommand;
import me.funky.praxi.commands.user.duels.DuelCommand;
import me.funky.praxi.commands.user.duels.RematchCommand;
import me.funky.praxi.kit.Kit;
import me.funky.praxi.kit.KitTypeAdapter;
import me.funky.praxi.commands.admin.kits.KitCreateCommand;
import me.funky.praxi.commands.admin.kits.KitGetLoadoutCommand;
import me.funky.praxi.commands.admin.kits.KitsCommand;
import me.funky.praxi.commands.admin.kits.KitSetLoadoutCommand;
import me.funky.praxi.kit.KitEditorListener;
import me.funky.praxi.match.Match;
import me.funky.praxi.commands.user.match.SpectateCommand;
import me.funky.praxi.commands.user.match.StopSpectatingCommand;
import me.funky.praxi.match.MatchListener;
import me.funky.praxi.party.Party;
import me.funky.praxi.commands.user.party.PartyChatCommand;
import me.funky.praxi.commands.user.party.PartyCloseCommand;
import me.funky.praxi.commands.user.party.PartyCreateCommand;
import me.funky.praxi.commands.user.party.PartyDisbandCommand;
import me.funky.praxi.commands.user.party.PartyHelpCommand;
import me.funky.praxi.commands.user.party.PartyInfoCommand;
import me.funky.praxi.commands.user.party.PartyInviteCommand;
import me.funky.praxi.commands.user.party.PartyJoinCommand;
import me.funky.praxi.commands.user.party.PartyKickCommand;
import me.funky.praxi.commands.user.party.PartyLeaveCommand;
import me.funky.praxi.commands.user.party.PartyOpenCommand;
import me.funky.praxi.party.PartyListener;
import me.funky.praxi.profile.Profile;
import me.funky.praxi.commands.donater.FlyCommand;
import me.funky.praxi.profile.ProfileListener;
import me.funky.praxi.profile.hotbar.Hotbar;
import me.funky.praxi.commands.user.settings.ToggleDuelRequestsCommand;
import me.funky.praxi.commands.user.settings.ToggleScoreboardCommand;
import me.funky.praxi.commands.user.settings.ToggleSpectatorsCommand;
import me.funky.praxi.queue.QueueListener;
import me.funky.praxi.queue.QueueThread;
import me.funky.praxi.scoreboard.ScoreboardAdapter;
import me.funky.praxi.util.InventoryUtil;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoCredential;
import com.mongodb.ServerAddress;
import com.mongodb.client.MongoDatabase;
import java.util.Arrays;
import lombok.Getter;
import me.funky.praxi.util.command.Honcho;
import me.funky.praxi.util.config.BasicConfigurationFile;
import me.funky.praxi.util.menu.MenuListener;
import org.bukkit.Difficulty;
import org.bukkit.Material;
import org.bukkit.plugin.java.JavaPlugin;

public class Praxi extends JavaPlugin {

	private static Praxi praxi;

	@Getter private BasicConfigurationFile mainConfig;
	@Getter private BasicConfigurationFile arenasConfig;
	@Getter private BasicConfigurationFile kitsConfig;
	@Getter private BasicConfigurationFile eventsConfig;
	@Getter private MongoDatabase mongoDatabase;
	@Getter private Honcho honcho;
	@Getter private Essentials essentials;

	@Override
	public void onEnable() {
		praxi = this;
		getServer().getPluginManager().registerEvents(new MenuListener(), this);
		honcho = new Honcho(this);
		mainConfig = new BasicConfigurationFile(this, "config");
		arenasConfig = new BasicConfigurationFile(this, "arenas");
		kitsConfig = new BasicConfigurationFile(this, "kits");
		eventsConfig = new BasicConfigurationFile(this, "events");
		this.essentials = new Essentials(this);
		loadMongo();

		Hotbar.init();
		Kit.init();
		Arena.init();
		Profile.init();
		Match.init();
		Party.init();
		Event.init();
		EventGameMap.init();

		new Aether(this, new ScoreboardAdapter(), new AetherOptions().hook(true));
		new QueueThread().start();

		getHoncho().registerTypeAdapter(Arena.class, new ArenaTypeAdapter());
		getHoncho().registerTypeAdapter(ArenaType.class, new ArenaTypeTypeAdapter());
		getHoncho().registerTypeAdapter(Kit.class, new KitTypeAdapter());
		getHoncho().registerTypeAdapter(EventGameMap.class, new EventGameMapTypeAdapter());
		getHoncho().registerTypeAdapter(Event.class, new EventTypeAdapter());

		Arrays.asList(
				new ArenaAddKitCommand(),
				new ArenaRemoveKitCommand(),
				new ArenaCreateCommand(),
				new ArenaDeleteCommand(),
				new ArenaGenerateCommand(),
				new ArenaGenHelperCommand(),
				new ArenaSaveCommand(),
				new ArenasCommand(),
				new ArenaSelectionCommand(),
				new ArenaSetSpawnCommand(),
				new ArenaStatusCommand(),
				new DuelCommand(),
				new DuelAcceptCommand(),
				new EventAdminCommand(),
				new EventHelpCommand(),
				new EventCancelCommand(),
				new EventClearCooldownCommand(),
				new EventForceStartCommand(),
				new EventHostCommand(),
				new EventInfoCommand(),
				new EventJoinCommand(),
				new EventLeaveCommand(),
				new EventSetLobbyCommand(),
				new EventMapCreateCommand(),
				new EventMapDeleteCommand(),
				new EventMapsCommand(),
				new EventMapSetSpawnCommand(),
				new EventMapStatusCommand(),
				new EventMapVoteCommand(),
				new EventAddMapCommand(),
				new EventRemoveMapCommand(),
				new EventsCommand(),
				new RematchCommand(),
				new SpectateCommand(),
				new StopSpectatingCommand(),
				new FlyCommand(),
				new SetSpawnCommand(),
				new PartyChatCommand(),
				new PartyCloseCommand(),
				new PartyCreateCommand(),
				new PartyDisbandCommand(),
				new PartyHelpCommand(),
				new PartyInfoCommand(),
				new PartyInviteCommand(),
				new PartyJoinCommand(),
				new PartyKickCommand(),
				new PartyLeaveCommand(),
				new PartyOpenCommand(),
				new KitCreateCommand(),
				new KitGetLoadoutCommand(),
				new KitSetLoadoutCommand(),
				new KitsCommand(),
				new ViewInventoryCommand(),
				new MatchTestCommand(),
				new ToggleScoreboardCommand(),
				new ToggleSpectatorsCommand(),
				new ToggleDuelRequestsCommand(),
				new SuicideCommand()
		).forEach(command -> getHoncho().registerCommand(command));

		Arrays.asList(
				new KitEditorListener(),
				new PartyListener(),
				new ProfileListener(),
				new PartyListener(),
				new MatchListener(),
				new QueueListener(),
				new ArenaListener(),
				new EventGameListener()
		).forEach(listener -> getServer().getPluginManager().registerEvents(listener, this));

		Arrays.asList(
				Material.WORKBENCH,
				Material.STICK,
				Material.WOOD_PLATE,
				Material.WOOD_BUTTON,
				Material.SNOW_BLOCK
		).forEach(InventoryUtil::removeCrafting);

		// Set the difficulty for each world to HARD
		// Clear the droppedItems for each world
		getServer().getWorlds().forEach(world -> {
			world.setDifficulty(Difficulty.HARD);
			getEssentials().clearEntities(world);

		});
	}

	@Override
	public void onDisable() {
		Match.cleanup();
	}

	private void loadMongo() {
		if (mainConfig.getBoolean("MONGO.AUTHENTICATION.ENABLED")) {
			mongoDatabase = new MongoClient(
					new ServerAddress(
							mainConfig.getString("MONGO.HOST"),
							mainConfig.getInteger("MONGO.PORT")
					),
					MongoCredential.createCredential(
							mainConfig.getString("MONGO.AUTHENTICATION.USERNAME"),
							mainConfig.getString("MONGO.AUTHENTICATION.ADMIN"), mainConfig.getString("MONGO.AUTHENTICATION.PASSWORD").toCharArray()
					),
					MongoClientOptions.builder().build()
			).getDatabase(mainConfig.getString("MONGO.DATABASE"));
		} else {
			mongoDatabase = new MongoClient(mainConfig.getString("MONGO.HOST"), mainConfig.getInteger("MONGO.PORT"))
					.getDatabase(mainConfig.getString("MONGO.DATABASE"));
		}
	}

	public static Praxi get() {
		return praxi;
	}

}
