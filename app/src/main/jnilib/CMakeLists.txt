set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=c++11 -Wall")
AUX_SOURCE_DIRECTORY(cpp SRC)
AUX_SOURCE_DIRECTORY(include INCLUDE)
set(INCLUDE_PATH include)
include_directories(${INCLUDE_PATH})
add_library(skybox SHARED ${SRC} ${INCLUDE})
# add lib dependencies
target_link_libraries(skybox
                      android
                      log
                      EGL
                      GLESv3)
