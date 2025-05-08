#!/bin/bash

# 脚本名称: call_springboot_api.sh
# 功能: 定时调用Spring Boot接口并记录日志

# 配置变量
API_URL="http://localhost:8080/your/api/endpoint"  # Spring Boot接口地址
LOG_FILE="/var/log/springboot_api_call.log"       # 日志文件路径
TIMEOUT=10                                        # curl请求超时时间（秒）
RETRY_COUNT=3                                     # 失败重试次数
RETRY_DELAY=5                                     # 重试间隔（秒）

# 确保日志目录存在
LOG_DIR=$(dirname "$LOG_FILE")
if [ ! -d "$LOG_DIR" ]; then
    mkdir -p "$LOG_DIR"
    chmod 755 "$LOG_DIR"
fi

# 日志记录函数
log() {
    echo "[$(date '+%Y-%m-%d %H:%M:%S')] $1" >> "$LOG_FILE"
}

# 调用接口函数
call_api() {
    local attempt=1
    while [ $attempt -le $RETRY_COUNT ]; do
        log "Attempt $attempt: Calling API at $API_URL"
        RESPONSE=$(curl -s -m "$TIMEOUT" -w "%{http_code}" "$API_URL" -o /tmp/api_response.txt 2>> "$LOG_FILE")
        HTTP_CODE="${RESPONSE: -3}"  # 提取HTTP状态码

        if [ "$HTTP_CODE" -eq 200 ]; then
            log "Success: HTTP $HTTP_CODE, Response: $(cat /tmp/api_response.txt)"
            return 0
        else
            log "Failed: HTTP $HTTP_CODE, Response: $(cat /tmp/api_response.txt)"
            if [ $attempt -lt $RETRY_COUNT ]; then
                log "Retrying after $RETRY_DELAY seconds..."
                sleep $RETRY_DELAY
            fi
        fi
        ((attempt++))
    done
    log "Error: Failed to call API after $RETRY_COUNT attempts"
    return 1
}

# 主逻辑
log "Starting API call script"
call_api
EXIT_CODE=$?

if [ $EXIT_CODE -eq 0 ]; then
    log "Script executed successfully"
else
    log "Script failed with exit code $EXIT_CODE"
fi

# 清理临时文件
rm -f /tmp/api_response.txt

exit $EXIT_CODE