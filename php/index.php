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
    <title>Progetti PHP | Informatica 5IB</title>
    <style>
        :root {
            font-family: Inter, system-ui, sans-serif;
            color-scheme: dark;
            background: #121212;
            color: #f7f5f2;
            --muted: #afaaa5;
            --accent: #ff635c;
            --border: #383332;
        }

        * { box-sizing: border-box; }
        body { margin: 0; min-height: 100vh; }
        .wrap { width: min(100% - 2.5rem, 68rem); margin-inline: auto; }

        header { border-bottom: 1px solid var(--border); }
        header .wrap {
            min-height: 4.25rem;
            display: flex;
            align-items: center;
            gap: .75rem;
            font-weight: 650;
        }

        .mark {
            color: var(--accent);
            background: #2f201f;
            border: 1px solid #563130;
            border-radius: .55rem;
            padding: .4rem .5rem;
            font: 700 .9rem ui-monospace, monospace;
        }

        main { padding-block: clamp(3rem, 9vw, 7.5rem); }

        h1 {
            margin: 0;
            font-size: clamp(2.9rem, 8vw, 6.2rem);
            line-height: .98;
            letter-spacing: -.075em;
        }

        .intro { color: var(--muted); margin: 1.15rem 0 2.6rem; }
        .intro strong { color: #f7f5f2; font-weight: 600; }

        ul {
            list-style: none;
            padding: 0;
            margin: 0;
            display: grid;
            grid-template-columns: repeat(auto-fit, minmax(min(100%, 19rem), 1fr));
            gap: 1rem;
        }

        .project {
            position: relative;
            display: flex;
            flex-direction: column;
            justify-content: flex-end;
            min-height: 9rem;
            padding: 1.3rem 3.4rem 1.3rem 1.3rem;
            text-decoration: none;
            color: inherit;
            background: #1d1b1b;
            border: 1px solid var(--border);
            border-radius: .85rem;
            overflow-wrap: anywhere;
            transition: background-color .16s, border-color .16s;
        }

        .project:hover { background: #292020; border-color: var(--accent); }
        .project:focus-visible { outline: 2px solid var(--accent); outline-offset: 4px; }

        .title { font-size: clamp(1.2rem, 3vw, 1.55rem); font-weight: 680; }
        .path { color: var(--muted); margin-top: .4rem; font: .86rem ui-monospace, monospace; }
        .arrow { position: absolute; top: 1.25rem; right: 1.3rem; color: var(--accent); font-size: 1.4rem; }
        .empty { border: 1px dashed var(--border); border-radius: .85rem; padding: 1.3rem; line-height: 1.6; }
        code { color: var(--accent); }

        @media (prefers-reduced-motion: reduce) { .project { transition: none; } }
    </style>
</head>
<body>
<header>
    <div class="wrap">
        <span class="mark" aria-hidden="true">&lt;/&gt;</span>
        <span>Informatica 5IB</span>
    </div>
</header>
<main class="wrap">
    <h1>Progetti PHP</h1>
    <p class="intro"><strong><?= $count ?></strong> <?= $count === 1 ? 'progetto disponibile' : 'progetti disponibili' ?></p>
    <?php if ($projects === []): ?>
        <p class="empty">Aggiungi una cartella con <code>index.php</code> o <code>public/index.php</code>, poi ricarica la pagina.</p>
    <?php else: ?>
        <ul>
            <?php foreach ($projects as $folder => $project): ?>
                <li>
                    <a class="project" href="<?= escape($project['url']) ?>">
                        <span class="title"><?= escape($project['title']) ?></span>
                        <span class="path"><?= escape((string) $folder) ?>/</span>
                        <span class="arrow" aria-hidden="true">&rarr;</span>
                    </a>
                </li>
            <?php endforeach; ?>
        </ul>
    <?php endif; ?>
</main>
</body>
</html>
