<?php
declare(strict_types=1);

$projects = [];
foreach (new DirectoryIterator(__DIR__) as $entry) {
    $folder = $entry->getFilename();
    if ($folder[0] === '.' || $entry->isLink() || !$entry->isDir()) {
        continue;
    }

    $path = $entry->getPathname();
    if (is_file($path . '/public/index.php')) {
        $entryPath = '/public/';
    } elseif (is_file($path . '/index.php')) {
        $entryPath = '/';
    } else {
        continue;
    }

    $title = preg_replace('/([a-z0-9])([A-Z])/', '$1 $2', $folder);
    $title = trim(str_replace(['-', '_'], ' ', $title));
    $projects[$folder] = [
        'title' => ucfirst($title === '' ? $folder : $title),
        'url' => './' . rawurlencode($folder) . $entryPath,
        'entry' => $folder . $entryPath . 'index.php',
    ];
}
uksort($projects, 'strnatcasecmp');
$count = count($projects);

function escape(string $text): string
{
    return htmlspecialchars($text, ENT_QUOTES | ENT_SUBSTITUTE, 'UTF-8');
}
?>
<!doctype html>
<html lang="it">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <meta name="color-scheme" content="dark">
    <meta name="theme-color" content="#08080a">
    <title>Progetti PHP | Informatica 5IB</title>
    <link rel="stylesheet" href="https://fonts.googleapis.com/css2?family=Barlow+Condensed:wght@700;800&family=Inter:wght@400;600;700&display=swap">
    <style>
        :root {
            color-scheme: dark;
            font-family: Inter, system-ui, sans-serif;
            --display: "Barlow Condensed", "Arial Narrow", "Liberation Sans Narrow", sans-serif;
            --mono: "IBM Plex Mono", ui-monospace, monospace;
            --background: #08080a;
            --card: #101013;
            --border: #27262b;
            --text: #f7f5f7;
            --muted: #aaa6b1;
            --red: #ff3c48;
            --php: #aaa2e6;
        }

        * { box-sizing: border-box; }

        body {
            min-height: 100vh;
            margin: 0;
            display: flex;
            flex-direction: column;
            color: var(--text);
            background:
                radial-gradient(ellipse at 83% 4%, #777bb521, transparent 34rem),
                radial-gradient(circle, #ffffff10 1px, transparent 1px) 0 0 / 26px 26px,
                var(--background);
        }

        .wrap { width: min(100% - 3rem, 66rem); margin-inline: auto; }
        main { flex: 1; padding-block: clamp(3rem, 7vw, 6.25rem); }

        .hero { border-left: 3px solid var(--red); padding-left: clamp(1rem, 3vw, 1.75rem); }
        .school {
            margin: 0;
            color: var(--red);
            font-size: .78rem;
            font-weight: 700;
            letter-spacing: .2em;
            text-transform: uppercase;
        }

        h1 {
            margin: .1em 0 .16em;
            font: 800 clamp(3.25rem, 9.5vw, 7.25rem)/.9 var(--display);
            letter-spacing: -.025em;
            text-transform: uppercase;
        }

        h1 span { color: var(--red); }
        .summary { margin: 0; color: var(--muted); line-height: 1.6; }
        .summary strong { color: var(--php); font-weight: 600; }

        .projects {
            list-style: none;
            padding: 0;
            margin: clamp(2.2rem, 6vw, 4.5rem) 0 0;
            display: grid;
            grid-template-columns: repeat(auto-fill, minmax(min(100%, 18rem), 1fr));
            gap: 1rem;
        }

        .project {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            height: 100%;
            min-height: 12rem;
            padding: 1.45rem;
            color: inherit;
            text-decoration: none;
            background: linear-gradient(140deg, #777bb514, transparent 60%), var(--card);
            border: 1px solid var(--border);
            border-radius: .75rem;
            overflow-wrap: anywhere;
            transition: border-color .18s, transform .18s;
        }

        .project:hover { border-color: var(--red); transform: translateY(-3px); }
        a:focus-visible { outline: 2px solid var(--php); outline-offset: 4px; }
        .entry {
            margin-bottom: .8rem;
            color: var(--php);
            font: .76rem/1.4 var(--mono);
        }

        .title { font-size: 1.26rem; font-weight: 700; }
        .action {
            margin-top: auto;
            padding-top: 1.5rem;
            color: var(--red);
            font-size: .75rem;
            font-weight: 700;
            letter-spacing: .1em;
            text-transform: uppercase;
        }

        .action span { display: inline-block; margin-left: .35rem; }
        .empty {
            margin-top: 2.5rem;
            border: 1px dashed var(--border);
            border-radius: .75rem;
            padding: 1.35rem;
            color: var(--muted);
            line-height: 1.65;
        }

        code { color: var(--php); font-family: var(--mono); }
        footer { border-top: 1px solid var(--border); background: var(--background); }
        footer .wrap {
            display: flex;
            align-items: center;
            justify-content: space-between;
            flex-wrap: wrap;
            gap: .8rem 1.2rem;
            padding-block: 1.35rem;
            color: var(--muted);
            font-size: .75rem;
        }

        footer a { color: var(--muted); text-underline-offset: .2em; }
        footer a:hover { color: var(--red); }

        @media (prefers-reduced-motion: reduce) {
            .project { transition: none; }
            .project:hover { transform: none; }
        }
    </style>
</head>
<body>
<main class="wrap">
    <header class="hero">
        <p class="school">Informatica 5IB</p>
        <h1>Progetti <span>PHP</span></h1>
        <p class="summary"><strong><?= $count ?></strong> <?= $count === 1 ? 'progetto disponibile' : 'progetti disponibili' ?></p>
    </header>
    <?php if ($projects === []): ?>
        <p class="empty">Aggiungi una cartella con <code>index.php</code> o <code>public/index.php</code>, poi ricarica la pagina.</p>
    <?php else: ?>
        <ul class="projects">
            <?php foreach ($projects as $project): ?>
                <li>
                    <a class="project" href="<?= escape($project['url']) ?>">
                        <span class="entry"><?= escape($project['entry']) ?></span>
                        <span class="title"><?= escape($project['title']) ?></span>
                        <span class="action">Esplora <span aria-hidden="true">&rarr;</span></span>
                    </a>
                </li>
            <?php endforeach; ?>
        </ul>
    <?php endif; ?>
</main>
<footer>
    <div class="wrap">
        <span>Riccardo Tonini · 5IB · ITIS C. Zuccante</span>
        <a href="https://quarta.rtonini.it/">Archivio 4IB</a>
    </div>
</footer>
</body>
</html>
