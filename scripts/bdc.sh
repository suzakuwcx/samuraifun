#!/bin/sh
: "
bdc - bdstuio compiler 

Compile bdstudio project to java code

Usage: ./scripts/bdc.sh [function file]

For bdstudio, see
<https://eszesbalint.github.io/bdstudio/editor>
"

set -e
. scripts/lib/message.sh

[[ -z "${EDITOR}" ]] && EDITOR=nano
colorize

function _vipe() {
    local file="/tmp/bdc.${RANDOM}"
    local output
    "${EDITOR}" "${file}"
    if [[ -f "${file}" ]]; then
        cat "${file}"
        rm -f "${file}"
    fi
}

# 需要，按帧提取 transformation, 然后生成代码
function generate_code() {
    local file="$1"

cat << EOF
    /* Generate by bdc.sh */
    @Override
    public void run() {
        Matrix4f mat;
        Transformation transformation;
        switch (tick) {
EOF

    local item
    local transformation
    local i=0

    cat "${file}" | while read l; do 
        item="$(echo "${l}" | cut -c 41- | grep -Eo "item:\{id:\"minecraft:[a-z_]+\"" | grep -Eo "minecraft:[a-z_]+" | cut -d ':' -f 2)"
        transformation="$(echo "${l}" | cut -c 41- | grep -Eo "transformation:\[.*\]" | cut -d '[' -f 2 | cut -d ']' -f 1)"
cat << EOF
            case ${i}:
                mat = new Matrix4f(${transformation});
                transformation = ServerBus.newTransformation(mat.transpose());
                display.setInterpolationDelay(0);
                display.setInterpolationDuration(4);
                display.setTransformation(transformation);
                break;
EOF
    i="$(expr 1 + $i)"
    done

cat << EOF
            default:
                display.remove();
                task_mapper.remove(player);
                return;
        }
        if (tick == 0)
            Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 1);
        else
            Bukkit.getScheduler().runTaskLater(ServerBus.getPlugin(), this, 4);
        ++tick;
    }    
EOF
}

function main() {
    local file=${1}
    if [[ ! -f "${file}" ]]; then error "No Such file or directory: \"${file}\""; exit 1; fi

    generate_code "${file}"
}

main "$@"
