# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

This is a Spring Boot 2.7.6 backend application for a picture management platform (yu-picture-backend). It provides REST APIs for user management, picture upload/editing, space management, and collaborative editing features.

## Build and Run Commands

```bash
# Build the project
mvn clean package

# Run tests
mvn test

# Run the application
mvn spring-boot:run

# Run specific test class
mvn test -Dtest=YuPictureBackendApplicationTests
```

## Architecture

### Layer Structure

- **Controller**: REST API endpoints (`com.yupi.yupicturebackend.controller`)
- **Service**: Business logic (`com.yupi.yupicturebackend.service` and `impl`)
- **Manager**: Cross-cutting concerns (`com.yupi.yupicturebackend.manager`)
  - `upload`: Picture upload strategies (File, URL)
  - `auth`: Space user authentication and authorization
  - `sharding`: Dynamic table partitioning for pictures
  - `websocket`: Real-time collaborative editing with Disruptor
- **Mapper**: Database access using MyBatis Plus (`com.yupi.yupicturebackend.mapper`)
- **Model**: Entities, DTOs, VOs, Enums (`com.yupi.yupicturebackend.model`)
- **API**: External service integrations (Aliyun AI, Baidu/So image search)

### Key Technologies

- **MyBatis Plus**: ORM with logical delete (`isDelete` field)
- **Sa-Token**: Authentication/authorization with Redis integration
- **ShardingSphere**: Dynamic table sharding for `picture` table by `spaceId`
- **Disruptor**: High-performance ring buffer for WebSocket event processing
- **WebSocket**: Real-time collaborative picture editing
- **Knife4j**: API documentation at `/doc.html`

### Authentication & Authorization

Two separate auth systems:
1. **User Auth**: Sa-Token for global user authentication (`StpKit`)
2. **Space Auth**: Role-based permissions within spaces (`SpaceUserAuthManager`)

Space roles are defined in `biz/spaceUserAuthConfig.json` and include:
- `admin`: Full permissions
- `editor`: Edit permissions
- `viewer`: View-only permissions

Use `@SaSpaceCheckPermission` annotation for space-level permission checks.

### Dynamic Sharding

The `picture` table is dynamically sharded by `spaceId`. When a new team space is created, a new table `picture_{spaceId}` is created. The sharding is handled by:
- `PictureShardingAlgorithm`: Custom sharding algorithm
- `DynamicShardingManager`: Manages dynamic table creation and ShardingSphere config updates

Note: `DynamicShardingManager` is currently disabled (`@Component` commented out) in `YuPictureBackendApplication.java`.

### WebSocket Collaborative Editing

Real-time picture editing uses:
- `PictureEditEvent`: Event model
- `PictureEditEventProducer`: Publishes events to Disruptor ring buffer
- `PictureEditEventWorkHandler`: Processes events from ring buffer
- `PictureEditEventDisruptorConfig`: Configures Disruptor with 256K buffer size

### Configuration

- **Server**: Port 8123, context path `/api`
- **Database**: MySQL `yu_picture` on localhost:3306
- **Redis**: localhost:6379 for session storage and Sa-Token
- **Session**: 30-day timeout, stored in Redis
- **File Upload**: Max 10MB

### Important Notes

- `YuPictureBackendApplication.java` excludes `ShardingSphereAutoConfiguration` and `SessionAutoConfiguration` by default
- MyBatis Plus is configured with `map-underscore-to-camel-case: false`
- All entities use `@TableLogic` for soft deletes
- Use `@AuthCheck(mustRole = "admin")` for admin-only endpoints
- Space permissions are checked via `SpaceUserAuthManager.getPermissionList()`