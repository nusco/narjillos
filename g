# So that I can pass parameters to Gradle tasks by typing (for example):
#
#   g backlog 3
#
# rather than:
#
#   gradle backlog -Pargs="3"

gradle --quiet --no-daemon $1 -Pargs="$2 $3 $4 $5 $6 $7"

