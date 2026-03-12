export default function Cell({ value, isActive, onClick, onHover }) {
  const className = [
    'SB',
    isActive ? 'activeCell' : '',
  ].join(' ');
  
  return (
    <div className={className} onClick={onClick} onMouseOver={onHover}>
      {value}
    </div>
  );
}
