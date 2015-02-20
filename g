# So that I can pass parameters to Gradle tasks by typing (for example):
#
#   g bl 3
#
# rather than:
#
#   gradle bl -Pargs="3"

gradle $1 -Pargs="$2 $3 $4 $5 $6 $7"

