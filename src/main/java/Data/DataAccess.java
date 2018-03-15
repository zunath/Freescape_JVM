package Data;
import Data.Repository.DatabaseRepository;
import Entities.*;
import Enumerations.PerkExecutionTypeID;
import Helper.ErrorHelper;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.ini4j.Ini;

import java.io.File;

public class DataAccess {
    private static String _host;
    private static String _username;
    private static String _password;
    private static String _schema;

    private static SessionFactory _sessionFactory;

    public static void Initialize()
    {
        try
        {
            Ini ini = new Ini(new File("/nwn/home/jvm/database.ini"));
            _host = ini.get("JavaDB", "server");
            _username = ini.get("JavaDB", "user");
            _password = ini.get("JavaDB", "pass");
            _schema = ini.get("JavaDB", "db");
        }
        catch (Exception ex)
        {
            ErrorHelper.HandleException(ex, "DataAccess Initialize()");
        }

        CreateSessionFactory();
    }


    private static void CreateSessionFactory()
    {
        Configuration _configuration = new Configuration();

        _configuration.setProperty("hibernate.connection.driver_class", "com.microsoft.sqlserver.jdbc.SQLServerDriver");
        _configuration.setProperty("hibernate.connection.url", "jdbc:sqlserver://" + _host + ";databaseName=" + _schema + ";");
        _configuration.setProperty("hibernate.connection.username", _username);
        _configuration.setProperty("hibernate.connection.password", _password);
        _configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.SQLServerDialect");
        _configuration.setProperty("hibernate.cache.use_second_level_cache", "false");
        _configuration.setProperty("hibernate.cache.use_query_cache", "false");
        _configuration.setProperty("hibernate.current_session_context_class", "thread");
        _configuration.setProperty("hibernate.c3p0.timeout", "300");

        // Link all DB entities to the configuration here.
        _configuration.addAnnotatedClass(ActivePlayerEntity.class);
        _configuration.addAnnotatedClass(AuthorizedDMEntity.class);
        _configuration.addAnnotatedClass(BaseItemTypeEntity.class);
        _configuration.addAnnotatedClass(BuildPrivacyEntity.class);
        _configuration.addAnnotatedClass(ChatChannelEntity.class);
        _configuration.addAnnotatedClass(ChatLogEntity.class);
        _configuration.addAnnotatedClass(ClientLogEventEntity.class);
        _configuration.addAnnotatedClass(ClientLogEventTypeEntity.class);
        _configuration.addAnnotatedClass(ConstructionSiteComponentEntity.class);
        _configuration.addAnnotatedClass(ConstructionSiteEntity.class);
        _configuration.addAnnotatedClass(CooldownCategoryEntity.class);
        _configuration.addAnnotatedClass(CraftBlueprintCategoryEntity.class);
        _configuration.addAnnotatedClass(CraftBlueprintEntity.class);
        _configuration.addAnnotatedClass(CraftComponentEntity.class);
        _configuration.addAnnotatedClass(CraftEntity.class);
        _configuration.addAnnotatedClass(CraftLevelEntity.class);
        _configuration.addAnnotatedClass(CreatureEntity.class);
        _configuration.addAnnotatedClass(CustomEffectEntity.class);
        _configuration.addAnnotatedClass(DMRoleEntity.class);
        _configuration.addAnnotatedClass(DatabaseRepository.class);
        _configuration.addAnnotatedClass(FameRegionEntity.class);
        _configuration.addAnnotatedClass(ItemEntity.class);
        _configuration.addAnnotatedClass(ItemTypeEntity.class);
        _configuration.addAnnotatedClass(KeyItemCategoryEntity.class);
        _configuration.addAnnotatedClass(KeyItemEntity.class);
        _configuration.addAnnotatedClass(LootTableEntity.class);
        _configuration.addAnnotatedClass(LootTableItemEntity.class);
        _configuration.addAnnotatedClass(NPCGroupEntity.class);
        _configuration.addAnnotatedClass(PCBlueprintEntity.class);
        _configuration.addAnnotatedClass(PCCooldownEntity.class);
        _configuration.addAnnotatedClass(PCCorpseEntity.class);
        _configuration.addAnnotatedClass(PCCorpseItemEntity.class);
        _configuration.addAnnotatedClass(PCCraftEntity.class);
        _configuration.addAnnotatedClass(PCCustomEffectEntity.class);
        _configuration.addAnnotatedClass(PCKeyItemEntity.class);
        _configuration.addAnnotatedClass(PCMigrationEntity.class);
        _configuration.addAnnotatedClass(PCMigrationItemEntity.class);
        _configuration.addAnnotatedClass(PCOutfitEntity.class);
        _configuration.addAnnotatedClass(PCOverflowItemEntity.class);
        _configuration.addAnnotatedClass(PCPerkHeaderEntity.class);
        _configuration.addAnnotatedClass(PCPerksEntity.class);
        _configuration.addAnnotatedClass(PCQuestKillTargetProgressEntity.class);
        _configuration.addAnnotatedClass(PCQuestStatusEntity.class);
        _configuration.addAnnotatedClass(PCRegionalFameEntity.class);
        _configuration.addAnnotatedClass(PCSearchSiteEntity.class);
        _configuration.addAnnotatedClass(PCSearchSiteItemEntity.class);
        _configuration.addAnnotatedClass(PCSkillEntity.class);
        _configuration.addAnnotatedClass(PCTerritoryFlagEntity.class);
        _configuration.addAnnotatedClass(PCTerritoryFlagPermissionEntity.class);
        _configuration.addAnnotatedClass(PCTerritoryFlagStructureEntity.class);
        _configuration.addAnnotatedClass(PCTerritoryFlagStructureItemEntity.class);
        _configuration.addAnnotatedClass(PCTerritoryFlagsStructuresResearchQueueEntity.class);
        _configuration.addAnnotatedClass(PerkCategoryEntity.class);
        _configuration.addAnnotatedClass(PerkEntity.class);
        _configuration.addAnnotatedClass(PerkExecutionTypeID.class);
        _configuration.addAnnotatedClass(PerkLevelEntity.class);
        _configuration.addAnnotatedClass(PerkLevelSkillRequirementEntity.class);
        _configuration.addAnnotatedClass(PlayerEntity.class);
        _configuration.addAnnotatedClass(QuestEntity.class);
        _configuration.addAnnotatedClass(QuestKillTargetListEntity.class);
        _configuration.addAnnotatedClass(QuestPrerequisiteEntity.class);
        _configuration.addAnnotatedClass(QuestRequiredItemListEntity.class);
        _configuration.addAnnotatedClass(QuestRequiredKeyItemListEntity.class);
        _configuration.addAnnotatedClass(QuestRewardItemEntity.class);
        _configuration.addAnnotatedClass(QuestStateEntity.class);
        _configuration.addAnnotatedClass(QuestTypeEntity.class);
        _configuration.addAnnotatedClass(ResearchBlueprintEntity.class);
        _configuration.addAnnotatedClass(ServerConfigurationEntity.class);
        _configuration.addAnnotatedClass(SkillCategoryEntity.class);
        _configuration.addAnnotatedClass(SkillEntity.class);
        _configuration.addAnnotatedClass(SkillXPRequirementEntity.class);
        _configuration.addAnnotatedClass(SpawnTableCreatureEntity.class);
        _configuration.addAnnotatedClass(SpawnTableEntity.class);
        _configuration.addAnnotatedClass(StorageContainerEntity.class);
        _configuration.addAnnotatedClass(StorageItemEntity.class);
        _configuration.addAnnotatedClass(StructureBlueprintEntity.class);
        _configuration.addAnnotatedClass(StructureCategoryEntity.class);
        _configuration.addAnnotatedClass(StructureComponentEntity.class);
        _configuration.addAnnotatedClass(StructureQuickBuildAuditEntity.class);
        _configuration.addAnnotatedClass(TerritoryFlagPermissionEntity.class);
        _configuration.addAnnotatedClass(TotalSkillXPEntity.class);

        ServiceRegistry _serviceRegistry = new StandardServiceRegistryBuilder().applySettings(
                _configuration.getProperties()).build();

        _sessionFactory = _configuration.buildSessionFactory(_serviceRegistry);
    }

    public static Session getSession()
    {
        return _sessionFactory.getCurrentSession();
    }
}
