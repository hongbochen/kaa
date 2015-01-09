/*
 * Copyright 2014 CyberVision, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.kaaproject.kaa.server.admin.client.mvp.data;

import java.util.ArrayList;
import java.util.List;

import org.kaaproject.kaa.common.dto.ApplicationDto;
import org.kaaproject.kaa.common.dto.ConfigurationDto;
import org.kaaproject.kaa.common.dto.ConfigurationSchemaDto;
import org.kaaproject.kaa.common.dto.EndpointGroupDto;
import org.kaaproject.kaa.common.dto.NotificationDto;
import org.kaaproject.kaa.common.dto.NotificationSchemaDto;
import org.kaaproject.kaa.common.dto.ProfileFilterDto;
import org.kaaproject.kaa.common.dto.ProfileSchemaDto;
import org.kaaproject.kaa.common.dto.SchemaDto;
import org.kaaproject.kaa.common.dto.StructureRecordDto;
import org.kaaproject.kaa.common.dto.TopicDto;
import org.kaaproject.kaa.common.dto.admin.RecordKey.RecordFiles;
import org.kaaproject.kaa.common.dto.admin.SchemaVersions;
import org.kaaproject.kaa.common.dto.admin.SdkPlatform;
import org.kaaproject.kaa.common.dto.admin.TenantUserDto;
import org.kaaproject.kaa.common.dto.admin.UserDto;
import org.kaaproject.kaa.common.dto.event.AefMapInfoDto;
import org.kaaproject.kaa.common.dto.event.ApplicationEventFamilyMapDto;
import org.kaaproject.kaa.common.dto.event.EcfInfoDto;
import org.kaaproject.kaa.common.dto.event.EventClassDto;
import org.kaaproject.kaa.common.dto.event.EventClassFamilyDto;
import org.kaaproject.kaa.common.dto.event.EventClassType;
import org.kaaproject.kaa.common.dto.event.EventSchemaVersionDto;
import org.kaaproject.kaa.common.dto.logs.LogAppenderDto;
import org.kaaproject.kaa.common.dto.logs.LogSchemaDto;
import org.kaaproject.kaa.server.admin.client.mvp.event.data.DataEvent;
import org.kaaproject.kaa.server.admin.shared.logs.LogAppenderFormWrapper;
import org.kaaproject.kaa.server.admin.shared.logs.LogAppenderInfoDto;
import org.kaaproject.kaa.server.admin.shared.properties.PropertiesDto;
import org.kaaproject.kaa.server.admin.shared.services.KaaAdminServiceAsync;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.web.bindery.event.shared.EventBus;

public class DataSource {

    private final KaaAdminServiceAsync rpcService;
    private final EventBus eventBus;

    private List<TenantUserDto> tenants;

    private List<ApplicationDto> applications;

    private List<UserDto> users;

    private List<EventClassFamilyDto> ecfs;
    
    private List<LogAppenderInfoDto> appenderInfos;

    public DataSource(KaaAdminServiceAsync rpcService, EventBus eventBus) {
        this.rpcService = rpcService;
        this.eventBus = eventBus;
    }

    public void getUserProfile(
            final AsyncCallback<UserDto> callback) {
        rpcService.getUserProfile(
                new DataCallback<UserDto>(callback) {
                    @Override
                    protected void onResult(UserDto result) {
                    }
                });
    }

    public void editUserProfile(UserDto user,
            final AsyncCallback<UserDto> callback) {
        rpcService.editUserProfile(user,
                new DataCallback<UserDto>(callback) {
                    @Override
                    protected void onResult(UserDto result) {
                    }
                });
    }
    
    public void getMailProperties(
            final AsyncCallback<PropertiesDto> callback) {
        rpcService.getMailProperties(
                new DataCallback<PropertiesDto>(callback) {
                    @Override
                    protected void onResult(PropertiesDto result) {
                    }
                });
    }

    public void editMailProperties(PropertiesDto mailProperties,
            final AsyncCallback<PropertiesDto> callback) {
        rpcService.editMailProperties(mailProperties,
                new DataCallback<PropertiesDto>(callback) {
                    @Override
                    protected void onResult(PropertiesDto result) {
                    }
                });
    }
    
    public void getGeneralProperties(
            final AsyncCallback<PropertiesDto> callback) {
        rpcService.getGeneralProperties(
                new DataCallback<PropertiesDto>(callback) {
                    @Override
                    protected void onResult(PropertiesDto result) {
                    }
                });
    }

    public void editGeneralProperties(PropertiesDto mailProperties,
            final AsyncCallback<PropertiesDto> callback) {
        rpcService.editGeneralProperties(mailProperties,
                new DataCallback<PropertiesDto>(callback) {
                    @Override
                    protected void onResult(PropertiesDto result) {
                    }
                });
    }

    public void loadTenants(final AsyncCallback<List<TenantUserDto>> callback) {
        loadTenants(callback, false);
    }

    public void loadTenants(final AsyncCallback<List<TenantUserDto>> callback,
            boolean refresh) {
        if (tenants == null || refresh) {
            tenants = new ArrayList<TenantUserDto>();
            rpcService.getTenants(new DataCallback<List<TenantUserDto>>(
                    callback) {
                @Override
                protected void onResult(List<TenantUserDto> result) {
                    tenants.addAll(result);
                    eventBus.fireEvent(new DataEvent(TenantUserDto.class, true));
                }
            });
        } else {
            if (callback != null) {
                callback.onSuccess(tenants);
            }
        }
    }

    private void refreshTenants() {
        loadTenants(null, true);
    }

    public void deleteTenant(String tenantId, final AsyncCallback<Void> callback) {
        rpcService.deleteTenant(tenantId, new DataCallback<Void>(callback) {
            @Override
            protected void onResult(Void result) {
                refreshTenants();
            }
        });
    }

    public void editTenant(TenantUserDto tenant,
            final AsyncCallback<TenantUserDto> callback) {
        rpcService.editTenant(tenant,
                new DataCallback<TenantUserDto>(callback) {
                    @Override
                    protected void onResult(TenantUserDto result) {
                        refreshTenants();
                    }
                });
    }

    public void getTenant(String tenantId,
            final AsyncCallback<TenantUserDto> callback) {
        rpcService.getTenant(tenantId,
                new DataCallback<TenantUserDto>(callback) {
                    @Override
                    protected void onResult(TenantUserDto result) {
                    }
                });
    }

    public void loadApplications(
            final AsyncCallback<List<ApplicationDto>> callback) {
        loadApplications(callback, false);
    }

    public void loadApplications(
            final AsyncCallback<List<ApplicationDto>> callback, boolean refresh) {
        if (applications == null || refresh) {
            applications = new ArrayList<ApplicationDto>();
            rpcService.getApplications(new DataCallback<List<ApplicationDto>>(
                    callback) {
                @Override
                protected void onResult(List<ApplicationDto> result) {
                    applications.addAll(result);
                    eventBus.fireEvent(new DataEvent(ApplicationDto.class, true));
                }
            });
        } else {
            if (callback != null) {
                callback.onSuccess(applications);
            }
        }
    }

    private void refreshApplications() {
        loadApplications(null, true);
    }

    public void deleteApplication(String applicationId,
            final AsyncCallback<Void> callback) {
        rpcService.deleteApplication(applicationId, new DataCallback<Void>(
                callback) {
            @Override
            protected void onResult(Void result) {
                refreshApplications();
            }
        });
    }

    public void editApplication(ApplicationDto application,
            final AsyncCallback<ApplicationDto> callback) {
        rpcService.editApplication(application,
                new DataCallback<ApplicationDto>(callback) {
                    @Override
                    protected void onResult(ApplicationDto result) {
                        refreshApplications();
                    }
                });
    }

    public void getApplication(String applicationId,
            final AsyncCallback<ApplicationDto> callback) {
        rpcService.getApplication(applicationId,
                new DataCallback<ApplicationDto>(callback) {
                    @Override
                    protected void onResult(ApplicationDto result) {
                    }
                });
    }

    public void getSchemaVersionsByApplicationId(String applicationId,
            final AsyncCallback<SchemaVersions> callback) {
        rpcService.getSchemaVersionsByApplicationId(applicationId,
                new DataCallback<SchemaVersions>(callback) {
            @Override
            protected void onResult(SchemaVersions result) {
            }
        });
    }

    public void getSdk(String applicationId,
            Integer configurationSchemaVersion,
            Integer profileSchemaVersion,
            Integer notificationSchemaVersion,
            SdkPlatform targetPlatform,
            List<String> aefMapIds,
            Integer logSchemaVersion,
            final AsyncCallback<String> callback) {
        rpcService.getSdk(applicationId,
                configurationSchemaVersion,
                profileSchemaVersion,
                notificationSchemaVersion,
                targetPlatform,
                aefMapIds,
                logSchemaVersion,
                new DataCallback<String>(callback) {
                    @Override
                    protected void onResult(String result) {
                    }
        });
    }

    public void getRecordLibrary(String applicationId,
            Integer logSchemaVersion, RecordFiles fileType,
            final AsyncCallback<String> callback) {
        rpcService.getRecordLibraryByApplicationIdAndSchemaVersion(applicationId, logSchemaVersion, fileType,
             new DataCallback<String>(callback) {
                        @Override
                        protected void onResult(String result) {
                        }
                    });
    }

    public void loadUsers(final AsyncCallback<List<UserDto>> callback) {
        loadUsers(callback, false);
    }

    public void loadUsers(final AsyncCallback<List<UserDto>> callback,
            boolean refresh) {
        if (users == null || refresh) {
            users = new ArrayList<UserDto>();
            rpcService.getUsers(new DataCallback<List<UserDto>>(callback) {
                @Override
                protected void onResult(List<UserDto> result) {
                    users.addAll(result);
                    eventBus.fireEvent(new DataEvent(UserDto.class, true));
                }
            });
        } else {
            if (callback != null) {
                callback.onSuccess(users);
            }
        }
    }

    private void refreshUsers() {
        loadUsers(null, true);
    }

    public void deleteUser(String userId, final AsyncCallback<Void> callback) {
        rpcService.deleteUser(userId, new DataCallback<Void>(callback) {
            @Override
            protected void onResult(Void result) {
                refreshUsers();
            }
        });
    }

    public void editUser(UserDto user, final AsyncCallback<UserDto> callback) {
        rpcService.editUser(user, new DataCallback<UserDto>(callback) {
            @Override
            protected void onResult(UserDto result) {
                refreshUsers();
            }
        });
    }

    public void getUser(String userId, final AsyncCallback<UserDto> callback) {
        rpcService.getUser(userId, new DataCallback<UserDto>(callback) {
            @Override
            protected void onResult(UserDto result) {
            }
        });
    }

    public void loadEcfs(
            final AsyncCallback<List<EventClassFamilyDto>> callback) {
        loadEcfs(callback, false);
    }

    public void loadEcfs(
            final AsyncCallback<List<EventClassFamilyDto>> callback, boolean refresh) {
        if (ecfs == null || refresh) {
            ecfs = new ArrayList<EventClassFamilyDto>();
            rpcService.getEventClassFamilies(new DataCallback<List<EventClassFamilyDto>>(
                    callback) {
                @Override
                protected void onResult(List<EventClassFamilyDto> result) {
                    ecfs.addAll(result);
                    eventBus.fireEvent(new DataEvent(EventClassFamilyDto.class, true));
                }
            });
        } else {
            if (callback != null) {
                callback.onSuccess(ecfs);
            }
        }
    }

    private void refreshEcfs() {
        loadEcfs(null, true);
    }

    public void editEcf(EventClassFamilyDto ecf,
            final AsyncCallback<EventClassFamilyDto> callback) {
        rpcService.editEventClassFamily(ecf,
                new DataCallback<EventClassFamilyDto>(callback) {
                    @Override
                    protected void onResult(EventClassFamilyDto result) {
                        refreshEcfs();
                    }
                });
    }

    public void getEcf(String ecfId,
            final AsyncCallback<EventClassFamilyDto> callback) {
        rpcService.getEventClassFamily(ecfId,
                new DataCallback<EventClassFamilyDto>(callback) {
                    @Override
                    protected void onResult(EventClassFamilyDto result) {
                    }
                });
    }

    public void addEcfSchema(String ecfId, String fileItemName,
            final AsyncCallback<Void> callback) {
        rpcService.addEventClassFamilySchema(ecfId, fileItemName,
                new DataCallback<Void>(callback) {
                    @Override
                    protected void onResult(Void result) {
                        eventBus.fireEvent(new DataEvent(EventSchemaVersionDto.class));
                    }
                });
    }

    public void getEventClassesByFamilyIdVersionAndType(String ecfId, int version, EventClassType type,
            final AsyncCallback<List<EventClassDto>> callback) {
        rpcService.getEventClassesByFamilyIdVersionAndType(ecfId, version, type,
                new DataCallback<List<EventClassDto>>(callback) {
            @Override
            protected void onResult(List<EventClassDto> result) {
            }
        });
    }

    public void loadProfileSchemas(String applicationId,
            final AsyncCallback<List<ProfileSchemaDto>> callback) {
        rpcService.getProfileSchemasByApplicationId(applicationId,
                new DataCallback<List<ProfileSchemaDto>>(callback) {
                    @Override
                    protected void onResult(List<ProfileSchemaDto> result) {
                    }
                });

    }

    public void editProfileSchema(ProfileSchemaDto profileSchema, String fileItemName,
            final AsyncCallback<ProfileSchemaDto> callback) {
        rpcService.editProfileSchema(profileSchema, fileItemName,
                new DataCallback<ProfileSchemaDto>(callback) {
                    @Override
                    protected void onResult(ProfileSchemaDto result) {
                        eventBus.fireEvent(new DataEvent(ProfileSchemaDto.class));
                    }
                });
    }

    public void getProfileSchema(String profileSchemaId,
            final AsyncCallback<ProfileSchemaDto> callback) {
        rpcService.getProfileSchema(profileSchemaId,
                new DataCallback<ProfileSchemaDto>(callback) {
                    @Override
                    protected void onResult(ProfileSchemaDto result) {
                    }
                });
    }

    public void loadConfigurationSchemas(String applicationId,
            final AsyncCallback<List<ConfigurationSchemaDto>> callback) {
        rpcService.getConfigurationSchemasByApplicationId(applicationId,
                new DataCallback<List<ConfigurationSchemaDto>>(callback) {
                    @Override
                    protected void onResult(List<ConfigurationSchemaDto> result) {
                    }
                });

    }

    public void editConfigurationSchema(
            ConfigurationSchemaDto configurationSchema, String fileItemName,
            final AsyncCallback<ConfigurationSchemaDto> callback) {
        rpcService.editConfigurationSchema(configurationSchema, fileItemName,
                new DataCallback<ConfigurationSchemaDto>(callback) {
                    @Override
                    protected void onResult(ConfigurationSchemaDto result) {
                        eventBus.fireEvent(new DataEvent(
                                ConfigurationSchemaDto.class));
                    }
                });
    }

    public void getConfigurationSchema(String configurationSchemaId,
            final AsyncCallback<ConfigurationSchemaDto> callback) {
        rpcService.getConfigurationSchema(configurationSchemaId,
                new DataCallback<ConfigurationSchemaDto>(callback) {
                    @Override
                    protected void onResult(ConfigurationSchemaDto result) {
                    }
                });
    }

    public void loadNotificationSchemas(String applicationId,
            final AsyncCallback<List<NotificationSchemaDto>> callback) {
        rpcService.getNotificationSchemasByApplicationId(applicationId,
                new DataCallback<List<NotificationSchemaDto>>(callback) {
                    @Override
                    protected void onResult(List<NotificationSchemaDto> result) {
                    }
                });

    }

    public void editNotificationSchema(
            NotificationSchemaDto notificationSchema, String fileItemName,
            final AsyncCallback<NotificationSchemaDto> callback) {
        rpcService.editNotificationSchema(notificationSchema, fileItemName,
                new DataCallback<NotificationSchemaDto>(callback) {
                    @Override
                    protected void onResult(NotificationSchemaDto result) {
                        eventBus.fireEvent(new DataEvent(
                                NotificationSchemaDto.class));
                    }
                });
    }

    public void getNotificationSchema(String notificationSchemaId,
            final AsyncCallback<NotificationSchemaDto> callback) {
        rpcService.getNotificationSchema(notificationSchemaId,
                new DataCallback<NotificationSchemaDto>(callback) {
                    @Override
                    protected void onResult(NotificationSchemaDto result) {
                    }
                });
    }

    public void loadLogSchemas(String applicationId,
            final AsyncCallback<List<LogSchemaDto>> callback) {
        rpcService.getLogSchemasByApplicationId(applicationId,
                new DataCallback<List<LogSchemaDto>>(callback) {
                    @Override
                    protected void onResult(List<LogSchemaDto> result) {
                    }
                });

    }

    public void loadLogSchemasVersion(String applicationId,
            final AsyncCallback<List<SchemaDto>> callback) {
        rpcService.getLogSchemasVersions(applicationId,
                new DataCallback<List<SchemaDto>>(callback) {
                    @Override
                    protected void onResult(List<SchemaDto> result) {
                    }
                });
    }

    public void editLogSchema(LogSchemaDto logSchema, String fileItemName,
            final AsyncCallback<LogSchemaDto> callback) {
        rpcService.editLogSchema(logSchema, fileItemName,
                new DataCallback<LogSchemaDto>(callback) {
                    @Override
                    protected void onResult(LogSchemaDto result) {
                        eventBus.fireEvent(new DataEvent(LogSchemaDto.class));
                    }
                });
    }

    public void getLogSchema(String logSchemaId,
            final AsyncCallback<LogSchemaDto> callback) {
        rpcService.getLogSchema(logSchemaId,
                new DataCallback<LogSchemaDto>(callback) {
                    @Override
                    protected void onResult(LogSchemaDto result) {
                    }
                });
    }

    public void loadApplicationEventFamilyMaps(String applicationId,
            final AsyncCallback<List<ApplicationEventFamilyMapDto>> callback) {
        rpcService.getApplicationEventFamilyMapsByApplicationId(applicationId,
                new DataCallback<List<ApplicationEventFamilyMapDto>>(callback) {
                    @Override
                    protected void onResult(List<ApplicationEventFamilyMapDto> result) {
                    }
                });

    }

    public void editApplicationEventFamilyMap(ApplicationEventFamilyMapDto applicationEventFamilyMap,
            final AsyncCallback<ApplicationEventFamilyMapDto> callback) {
        rpcService.editApplicationEventFamilyMap(applicationEventFamilyMap,
                new DataCallback<ApplicationEventFamilyMapDto>(callback) {
                    @Override
                    protected void onResult(ApplicationEventFamilyMapDto result) {
                        eventBus.fireEvent(new DataEvent(ApplicationEventFamilyMapDto.class));
                    }
                });
    }

    public void getApplicationEventFamilyMap(String applicationEventFamilyMapId,
            final AsyncCallback<ApplicationEventFamilyMapDto> callback) {
        rpcService.getApplicationEventFamilyMap(applicationEventFamilyMapId,
                new DataCallback<ApplicationEventFamilyMapDto>(callback) {
                    @Override
                    protected void onResult(ApplicationEventFamilyMapDto result) {
                    }
                });
    }

    public void getVacantEventClassFamilies(String applicationId,
            final AsyncCallback<List<EcfInfoDto>> callback) {
        rpcService.getVacantEventClassFamiliesByApplicationId(applicationId,
                new DataCallback<List<EcfInfoDto>>(callback) {
            @Override
            protected void onResult(List<EcfInfoDto> result) {
            }
        });
    }

    public void getAefMaps(String applicationId,
            final AsyncCallback<List<AefMapInfoDto>> callback) {
        rpcService.getEventClassFamiliesByApplicationId(applicationId,
                new DataCallback<List<AefMapInfoDto>>(callback) {
            @Override
            protected void onResult(List<AefMapInfoDto> result) {
            }
        });
    }

    public void loadEndpointGroups(String applicationId,
            final AsyncCallback<List<EndpointGroupDto>> callback) {
        rpcService.getEndpointGroupsByApplicationId(applicationId,
                new DataCallback<List<EndpointGroupDto>>(callback) {
                    @Override
                    protected void onResult(List<EndpointGroupDto> result) {
                    }
                });

    }

    public void deleteEndpointGroup(String endpointGroupId,
            final AsyncCallback<Void> callback) {
        rpcService.deleteEndpointGroup(endpointGroupId,
                new DataCallback<Void>(callback) {
                    @Override
                    protected void onResult(Void result) {
                        eventBus.fireEvent(new DataEvent(
                                EndpointGroupDto.class));
                    }
                });
    }

    public void editEndpointGroup(
            EndpointGroupDto endpointGroup,
            final AsyncCallback<EndpointGroupDto> callback) {
        rpcService.editEndpointGroup(endpointGroup,
                new DataCallback<EndpointGroupDto>(callback) {
                    @Override
                    protected void onResult(EndpointGroupDto result) {
                        eventBus.fireEvent(new DataEvent(
                                EndpointGroupDto.class));
                    }
                });
    }

    public void getEndpointGroup(String endpointGroupId,
            final AsyncCallback<EndpointGroupDto> callback) {
        rpcService.getEndpointGroup(endpointGroupId,
                new DataCallback<EndpointGroupDto>(callback) {
                    @Override
                    protected void onResult(EndpointGroupDto result) {
                    }
                });
    }

    public void loadProfileFilterRecords(String endpointGroupId, boolean includeDeprecated,
            final AsyncCallback<List<StructureRecordDto<ProfileFilterDto>>> callback) {
        rpcService.getProfileFilterRecordsByEndpointGroupId(endpointGroupId, includeDeprecated,
                new DataCallback<List<StructureRecordDto<ProfileFilterDto>>>(callback) {
                    @Override
                    protected void onResult(List<StructureRecordDto<ProfileFilterDto>> result) {
                    }
                });
    }

    public void getProfileFilterRecord(String schemaId, String endpointGroupId,
            final AsyncCallback<StructureRecordDto<ProfileFilterDto>> callback) {
        rpcService.getProfileFilterRecord(schemaId, endpointGroupId,
                new DataCallback<StructureRecordDto<ProfileFilterDto>>(callback) {
            @Override
            protected void onResult(StructureRecordDto<ProfileFilterDto> result) {
            }
        });
    }

    public void deleteProfileFilterRecord(String schemaId, String endpointGroupId,
            final AsyncCallback<Void> callback) {
        rpcService.deleteProfileFilterRecord(schemaId, endpointGroupId,
                new DataCallback<Void>(callback) {
            @Override
            protected void onResult(Void result) {
                eventBus.fireEvent(new DataEvent(
                        ProfileFilterDto.class));
            }
        });
    }

    public void editProfileFilter(ProfileFilterDto profileFilter,
            final AsyncCallback<ProfileFilterDto> callback) {
        rpcService.editProfileFilter(profileFilter,
                new DataCallback<ProfileFilterDto>(callback) {
                    @Override
                    protected void onResult(ProfileFilterDto result) {
                        eventBus.fireEvent(new DataEvent(ProfileFilterDto.class));
                    }
        });
    }

    public void activateProfileFilter(String profileFilterId,
            final AsyncCallback<ProfileFilterDto> callback) {
        rpcService.activateProfileFilter(profileFilterId,
                new DataCallback<ProfileFilterDto>(callback) {
                    @Override
                    protected void onResult(ProfileFilterDto result) {
                        eventBus.fireEvent(new DataEvent(ProfileFilterDto.class));
                    }
        });
    }

    public void deactivateProfileFilter(String profileFilterId,
            final AsyncCallback<ProfileFilterDto> callback) {
        rpcService.deactivateProfileFilter(profileFilterId,
                new DataCallback<ProfileFilterDto>(callback) {
                    @Override
                    protected void onResult(ProfileFilterDto result) {
                        eventBus.fireEvent(new DataEvent(ProfileFilterDto.class));
                    }
        });
    }

    public void loadConfigurationRecords(String endpointGroupId, boolean includeDeprecated,
            final AsyncCallback<List<StructureRecordDto<ConfigurationDto>>> callback) {
        rpcService.getConfigurationRecordsByEndpointGroupId(endpointGroupId, includeDeprecated,
                new DataCallback<List<StructureRecordDto<ConfigurationDto>>>(callback) {
                    @Override
                    protected void onResult(List<StructureRecordDto<ConfigurationDto>> result) {
                    }
                });
    }

    public void getConfigurationRecord(String schemaId, String endpointGroupId,
            final AsyncCallback<StructureRecordDto<ConfigurationDto>> callback) {
        rpcService.getConfigurationRecord(schemaId, endpointGroupId,
                new DataCallback<StructureRecordDto<ConfigurationDto>>(callback) {
            @Override
            protected void onResult(StructureRecordDto<ConfigurationDto> result) {
            }
        });
    }

    public void deleteConfigurationRecord(String schemaId, String endpointGroupId,
            final AsyncCallback<Void> callback) {
        rpcService.deleteConfigurationRecord(schemaId, endpointGroupId,
                new DataCallback<Void>(callback) {
            @Override
            protected void onResult(Void result) {
                eventBus.fireEvent(new DataEvent(
                        ConfigurationDto.class));
            }
        });
    }

    public void editConfiguration(ConfigurationDto configuration,
            final AsyncCallback<ConfigurationDto> callback) {
        rpcService.editConfiguration(configuration,
                new DataCallback<ConfigurationDto>(callback) {
                    @Override
                    protected void onResult(ConfigurationDto result) {
                        eventBus.fireEvent(new DataEvent(ConfigurationDto.class));
                    }
        });
    }

    public void activateConfiguration(String configurationId,
            final AsyncCallback<ConfigurationDto> callback) {
        rpcService.activateConfiguration(configurationId,
                new DataCallback<ConfigurationDto>(callback) {
                    @Override
                    protected void onResult(ConfigurationDto result) {
                        eventBus.fireEvent(new DataEvent(ConfigurationDto.class));
                    }
        });
    }

    public void deactivateConfiguration(String configurationId,
            final AsyncCallback<ConfigurationDto> callback) {
        rpcService.deactivateConfiguration(configurationId,
                new DataCallback<ConfigurationDto>(callback) {
                    @Override
                    protected void onResult(ConfigurationDto result) {
                        eventBus.fireEvent(new DataEvent(ConfigurationDto.class));
                    }
        });
    }

    public void getVacantProfileSchemas(String endpointGroupId,
            final AsyncCallback<List<SchemaDto>> callback) {
        rpcService.getVacantProfileSchemasByEndpointGroupId(endpointGroupId,
                new DataCallback<List<SchemaDto>>(callback) {
            @Override
            protected void onResult(List<SchemaDto> result) {
            }
        });
    }

    public void getVacantConfigurationSchemas(String endpointGroupId,
            final AsyncCallback<List<SchemaDto>> callback) {
        rpcService.getVacantConfigurationSchemasByEndpointGroupId(endpointGroupId,
                new DataCallback<List<SchemaDto>>(callback) {
            @Override
            protected void onResult(List<SchemaDto> result) {
            }
        });
    }

    public void getUserNotificationSchemas(String applicationId,
            final AsyncCallback<List<SchemaDto>> callback) {
        rpcService.getUserNotificationSchemasByApplicationId(applicationId,
                new DataCallback<List<SchemaDto>>(callback) {
            @Override
            protected void onResult(List<SchemaDto> result) {
            }
        });
    }

    public void loadTopics(String applicationId,
            final AsyncCallback<List<TopicDto>> callback) {
        rpcService.getTopicsByApplicationId(applicationId,
                new DataCallback<List<TopicDto>>(callback) {
                    @Override
                    protected void onResult(List<TopicDto> result) {
                    }
                });
    }

    public void loadTopicsByEndpointGroupId(String endpointGroupId,
            final AsyncCallback<List<TopicDto>> callback) {
        rpcService.getTopicsByEndpointGroupId(endpointGroupId,
                new DataCallback<List<TopicDto>>(callback) {
                    @Override
                    protected void onResult(List<TopicDto> result) {
                    }
                });
    }

    public void loadVacantTopicsByEndpointGroupId(String endpointGroupId,
            final AsyncCallback<List<TopicDto>> callback) {
        rpcService.getVacantTopicsByEndpointGroupId(endpointGroupId,
                new DataCallback<List<TopicDto>>(callback) {
                    @Override
                    protected void onResult(List<TopicDto> result) {
                    }
                });
    }

    public void deleteTopic(String topicId,
            final AsyncCallback<Void> callback) {
        rpcService.deleteTopic(topicId,
                new DataCallback<Void>(callback) {
                    @Override
                    protected void onResult(Void result) {
                        eventBus.fireEvent(new DataEvent(
                                TopicDto.class));
                    }
                });
    }

    public void editTopic(
            TopicDto topic,
            final AsyncCallback<TopicDto> callback) {
        rpcService.editTopic(topic,
                new DataCallback<TopicDto>(callback) {
                    @Override
                    protected void onResult(TopicDto result) {
                        eventBus.fireEvent(new DataEvent(
                                TopicDto.class));
                    }
                });
    }

    public void getTopic(String topicId,
            final AsyncCallback<TopicDto> callback) {
        rpcService.getTopic(topicId,
                new DataCallback<TopicDto>(callback) {
                    @Override
                    protected void onResult(TopicDto result) {
                    }
                });
    }

    public void addTopicToEndpointGroup(String endpointGroupId, String topicId,
            final AsyncCallback<Void> callback) {
        rpcService.addTopicToEndpointGroup(endpointGroupId, topicId,
                new DataCallback<Void>(callback) {
                    @Override
                    protected void onResult(Void result) {
                        eventBus.fireEvent(new DataEvent(
                                TopicDto.class));
                    }
                });
    }

    public void removeTopicFromEndpointGroup(String endpointGroupId, String topicId,
            final AsyncCallback<Void> callback) {
        rpcService.removeTopicFromEndpointGroup(endpointGroupId, topicId,
                new DataCallback<Void>(callback) {
                    @Override
                    protected void onResult(Void result) {
                        eventBus.fireEvent(new DataEvent(
                                TopicDto.class));
                    }
                });
    }

    public void sendNotification(
            NotificationDto notification, String fileItemName,
            final AsyncCallback<Void> callback) {
        rpcService.sendNotification(notification, fileItemName,
                new DataCallback<Void>(callback) {
                    @Override
                    protected void onResult(Void result) {
                    }
                });
    }

    public void loadLogAppenders(String applicationId,
            final AsyncCallback<List<LogAppenderDto>> callback) {
        rpcService.getLogAppendersByApplicationId(applicationId,
                new DataCallback<List<LogAppenderDto>>(callback) {
            @Override
            protected void onResult(List<LogAppenderDto> result) {
            }
        });
    }

    public void getLogAppenderForm(String appenderId,
            final AsyncCallback<LogAppenderFormWrapper> callback) {
        rpcService.getLogAppenderForm(appenderId,
                new DataCallback<LogAppenderFormWrapper>(callback) {
            @Override
            protected void onResult(LogAppenderFormWrapper result) {
            }
        });
    }

    public void editLogAppenderForm(LogAppenderFormWrapper dto,
            final AsyncCallback<LogAppenderFormWrapper> callback) {
        rpcService.editLogAppenderForm(dto,
                new DataCallback<LogAppenderFormWrapper>(callback) {
            @Override
            protected void onResult(LogAppenderFormWrapper result) {
            }
        });
    }

    public void removeLogAppender(String appenderId,
            final AsyncCallback<Void> callback) {
        rpcService.deleteLogAppender(appenderId,
                new DataCallback<Void>(callback) {
            @Override
            protected void onResult(Void result) {
                eventBus.fireEvent(new DataEvent(
                        LogAppenderDto.class));
            }
        });
    }
    
    public void loadAppenderInfos(
            final AsyncCallback<List<LogAppenderInfoDto>> callback) {
        if (appenderInfos == null) {
            appenderInfos = new ArrayList<LogAppenderInfoDto>();
            rpcService.getLogAppenderInfos(new DataCallback<List<LogAppenderInfoDto>>(callback) {
                @Override
                protected void onResult(List<LogAppenderInfoDto> result) {
                    appenderInfos.addAll(result);
                }
            });
        } else {
            if (callback != null) {
                callback.onSuccess(appenderInfos);
            }
        }
    }    

    abstract class DataCallback<T> implements AsyncCallback<T> {

        AsyncCallback<T> callback;

        DataCallback(AsyncCallback<T> callback) {
            this.callback = callback;
        }

        @Override
        public void onFailure(Throwable caught) {
            if (callback != null) {
                callback.onFailure(caught);
            }
        }

        @Override
        public void onSuccess(T result) {
            onResult(result);
            if (callback != null) {
                callback.onSuccess(result);
            }
        }

        protected abstract void onResult(T result);

    }

}