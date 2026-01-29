import BigBoard from './BigBoard';

export default function GiantBoard({
  smallBoards, bigBoard, activeBigs, onPlay, onHover, entering,
}) {
  const isActive = (r,c) => activeBigs.has(`${r},${c}`);
  return (
    <table id="giantBoard" className={`GB ${entering ? 'outAbove' : ''}`}>
      <tbody>
        {[0,1,2].map(r => (
          <tr key={r}>
            {[0,1,2].map(c => (
              <BigBoard
                key={c}
                r={r}
                c={c}
                board={smallBoards[r][c]}
                bigStatus={bigBoard[r][c]}
                isActive={isActive(r,c)}
                onHover={onHover}
                onCellClick={(sr,sc) => onPlay(r,c,sr,sc)}
              />
            ))}
          </tr>
        ))}
      </tbody>
    </table>
  );
}
