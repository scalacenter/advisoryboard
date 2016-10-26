# SCP-009: Maintain scala-lang, docs.scala-lang, scala.epfl.ch websites

## Proposer

Proposed by Lightbend, November 2016

credits:
* initial draft: Seth Tisue
* feedback: Adriaan Moors, Dale Wijnand, Eugene Yokota

## Abstract

We propose that the Scala Center assume responsibility for updating
and maintaining the scala-lang.org, docs.scala-lang.org, and
scala.epfl.ch websites.

These sites are Scala's public face to the world.  It's vital to
the health and growth of the Scala language and the Scala community
that we show the world the best face we can.

Note that we are not proposing a wholesale redesign and/or rethink of
the sites, or any substantial expansion.  The scope of the current
proposal is limited to updating and maintenance of existing content,
plus making whatever modest, targeted, specific additions and
improvements that can be done at limited cost.

## Proposal

Major areas of concern include:

* All three sites have outdated content
* Issues and pull requests need ongoing attention
* Comments need ongoing attention
* Infrastructure updates are needed

More detail on all of these follows. The specifics below aren't
meant to be binding; they're examples.

* All three sites have outdated content
    * scala-lang.org looks great and has a lot of valuable content,
      but it needs ongoing refreshing, with an emphasis on guiding
      newcomers to welcoming material that will get them up and
      running quickly.  For example, ongoing updates are needed
      regarding available courses and books, upcoming conferences,
      getting-started instructions, availability of alternative
      back-ends (Scala.js, Scala Native),
      Scala Fiddle, IDE and editor
      and build tool support, etc.
    * scala.epfl.ch was in good shape when the Center launched, but
      now lacks information about recent Scala Center projects and
      activities (visitors are unlikely to stumble across the advisory
      board meeting notes, which are too detailed for casual visitors
      anyway. there is Center news in scala-lang blog posts, but
      scala.epfl.ch doesn't link to those)
    * docs.scala-lang.org is the most neglected. Outdated content
      exists through the site.  The single most important feature, the
      "Tour of Scala", frequently gets very negative feedback from
      visitors new to the language; it needs an overhaul.  The look
      and feel of the site should be brought in line with
      scala-lang.org.
* Issues and pull requests need ongoing attention
    * Scala Center and Lightbend personnel do sometimes review and merge
      contributors' pull requests for the sites, but no one is
      primarily responsible and the handling has been intermittent and
      inconsistent.  This is discouraging for outside contributors.
* Comments need ongoing attention
    * The Disqus comments on docs.scala-lang need to be moderated for
      spam, tone, and relevancy.  Not all comments can or should
      be directly responded to by Scala Center personnel, but some
      should, for example when obvious flaws in content are pointed
      out.
    * We might also consider alternatives to Disqus.  For example,
      questions could be directed to Gitter; feedback and edits
      could be directed to GitHub issues and pull requests.
* Infrastructure updates are needed
    * scala-lang.org and docs.scala-lang use different versions of
      Jekyll, are inconsistent about whether bundler is used, etc.
      scala-lang.org is hosted on a legacy EPFL server shared with
      other sites and applications; it could be independently
      cloud-hosted.  The docs site takes a long time to build,
      which is discouraging for contributors; this should be fixed.

Some of these items involve improving the sites directly.  Others
would make it easier for community members to contribute their own
improvements.

There are GitHub tickets on some of these issues.  Tickets also exist
on many issues not specifically mentioned here.

## Cost

Since the scope of this proposal is reasonably limited, probably 0.5
FTE would be sufficient?  (Either by one person, or split among
multiple Scala Center personnel.)

Lightbend staff would continue to help out, as we have in the past.

## Timescales

The timescale is open-ended, as the proposal is for permanent ongoing
maintenance.
