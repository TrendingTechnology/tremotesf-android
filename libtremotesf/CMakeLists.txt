cmake_minimum_required(VERSION 3.10.2)

project(tremotesf CXX)

if (QT_HAS_ABI_SUFFIX)
    list(APPEND CMAKE_FIND_ROOT_PATH "${QT_DIR}/install-api${ANDROID_PLATFORM_LEVEL}")
else()
    list(APPEND CMAKE_FIND_ROOT_PATH "${QT_DIR}/install-${CMAKE_ANDROID_ARCH_ABI}")
endif()

set(CMAKE_INCLUDE_CURRENT_DIR ON)
set(CMAKE_AUTOMOC ON)

find_package(Qt5Core REQUIRED)
find_package(Qt5Concurrent REQUIRED)
find_package(Qt5Network REQUIRED)

add_library(${PROJECT_NAME} SHARED
        libtremotesf/peer.cpp
        libtremotesf/rpc.cpp
        libtremotesf/serversettings.cpp
        libtremotesf/serverstats.cpp
        libtremotesf/torrent.cpp
        libtremotesf/torrentfile.cpp
        libtremotesf/tracker.cpp
        jni/jnirpc.cpp
        jni/libtremotesf_wrap.cxx)

set_target_properties(${PROJECT_NAME} PROPERTIES
        CXX_STANDARD 17
        CXX_STANDARD_REQUIRED ON
        CXX_EXTENSIONS OFF
        INTERPROCEDURAL_OPTIMIZATION_RELEASE ON)

target_compile_definitions(${PROJECT_NAME} PRIVATE QT_DEPRECATED_WARNINGS QT_DISABLE_DEPRECATED_BEFORE=0x05f00)
target_compile_options(${PROJECT_NAME} PRIVATE -Wall -Wextra -pedantic)
target_include_directories(${PROJECT_NAME} PRIVATE ${Qt5Concurrent_INCLUDE_DIRS})
target_link_libraries(${PROJECT_NAME} Qt5::Core Qt5::Network)

# CMake overrides linker with -fuse-ld=gold when INTERPROCEDURAL_OPTIMIZATION is enabled. Bring it back
target_link_libraries(${PROJECT_NAME} -fuse-ld=lld)
